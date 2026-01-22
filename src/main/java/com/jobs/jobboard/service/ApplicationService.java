package com.jobs.jobboard.service;

import com.jobs.jobboard.entity.Application;
import com.jobs.jobboard.entity.JobVacancy;
import com.jobs.jobboard.entity.JobStatus;
import com.jobs.jobboard.entity.Role;
import com.jobs.jobboard.entity.User;
import com.jobs.jobboard.exception.BusinessException;
import com.jobs.jobboard.repository.ApplicationRepository;
import com.jobs.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final SecurityService securityService;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, 
                               JobRepository jobRepository,
                               SecurityService securityService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.securityService = securityService;
    }

    @Transactional
    public Application createApplication(Long jobId, String coverLetter) {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.CANDIDATE) {
            throw new BusinessException("Apenas candidatos podem se candidatar a vagas");
        }

        JobVacancy job = jobRepository.findByIdAndNotDeleted(jobId)
                .orElseThrow(() -> new BusinessException("Vaga não encontrada"));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new BusinessException("Não é possível se candidatar a uma vaga " + job.getStatus().name().toLowerCase());
        }

        if (applicationRepository.findByCandidateIdAndJobIdAndNotDeleted(currentUser.getId(), jobId).isPresent()) {
            throw new BusinessException("Você já se candidatou a esta vaga");
        }

        Application application = new Application();
        application.setCandidate(currentUser);
        application.setJobVacancy(job);
        application.setCoverLetter(coverLetter);

        return applicationRepository.save(application);
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("Candidatura não encontrada"));
    }

    public List<Application> getMyApplications() {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.CANDIDATE) {
            throw new BusinessException("Apenas candidatos podem visualizar suas candidaturas");
        }

        return applicationRepository.findByCandidateIdAndNotDeleted(currentUser.getId());
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        User currentUser = securityService.getCurrentUser();
        
        JobVacancy job = jobRepository.findByIdAndNotDeleted(jobId)
                .orElseThrow(() -> new BusinessException("Vaga não encontrada"));

        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem visualizar candidaturas de suas vagas");
        }

        if (!job.getCompany().getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Você não tem permissão para visualizar candidaturas desta vaga");
        }

        return applicationRepository.findByJobIdAndNotDeleted(jobId);
    }

    @Transactional
    public void deleteApplication(Long applicationId) {
        User currentUser = securityService.getCurrentUser();
        
        Application application = applicationRepository.findByIdAndNotDeleted(applicationId)
                .orElseThrow(() -> new BusinessException("Candidatura não encontrada"));

        if (currentUser.getRole() == Role.CANDIDATE) {
            if (!application.getCandidate().getId().equals(currentUser.getId())) {
                throw new BusinessException("Você não tem permissão para deletar esta candidatura");
            }
        }

        application.setDeletedAt(LocalDateTime.now());
        applicationRepository.save(application);
    }
}
