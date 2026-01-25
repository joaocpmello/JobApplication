package com.jobs.jobboard.service;

import com.jobs.jobboard.dto.response.ApplicationResponse;
import com.jobs.jobboard.dto.response.CompanySummaryResponse;
import com.jobs.jobboard.dto.response.JobSummaryResponse;
import com.jobs.jobboard.dto.response.UserResponse;
import com.jobs.jobboard.entity.*;
import com.jobs.jobboard.exception.BusinessException;
import com.jobs.jobboard.repository.ApplicationRepository;
import com.jobs.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ApplicationResponse createApplication(Long jobId, String coverLetter) {
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
        application.setStatus(ApplicationStatus.PENDING);

        return toResponse(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(Long id) {
        User currentUser = securityService.getCurrentUser();

        Application application = applicationRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("Candidatura não encontrada"));

        if (currentUser.getRole() == Role.ADMIN) {
            return toResponse(application);
        }

        if (currentUser.getRole() == Role.CANDIDATE) {
            if (!application.getCandidate().getId().equals(currentUser.getId())) {
                throw new BusinessException("Você não tem permissão para visualizar esta candidatura");
            }
            return toResponse(application);
        }

        if (currentUser.getRole() == Role.COMPANY) {
            Long ownerUserId = application.getJobVacancy().getCompany().getUser().getId();
            if (!ownerUserId.equals(currentUser.getId())) {
                throw new BusinessException("Você não tem permissão para visualizar esta candidatura");
            }
            return toResponse(application);
        }

        throw new BusinessException("Você não tem permissão para visualizar esta candidatura");
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {
        User currentUser = securityService.getCurrentUser();

        if (currentUser.getRole() != Role.CANDIDATE) {
            throw new BusinessException("Apenas candidatos podem visualizar suas candidaturas");
        }

        return applicationRepository.findByCandidateIdAndNotDeleted(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getApplicationsByJob(Long jobId, ApplicationStatus status, Pageable pageable) {
        User currentUser = securityService.getCurrentUser();

        JobVacancy job = jobRepository.findByIdAndNotDeleted(jobId)
                .orElseThrow(() -> new BusinessException("Vaga não encontrada"));

        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem visualizar candidaturas de suas vagas");
        }

        if (!job.getCompany().getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Você não tem permissão para visualizar candidaturas desta vaga");
        }

        return applicationRepository.findByJobIdAndStatusAndNotDeleted(jobId, status, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        User currentUser = securityService.getCurrentUser();

        Application application = applicationRepository.findByIdAndNotDeleted(applicationId)
                .orElseThrow(() -> new BusinessException("Candidatura não encontrada"));

        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem atualizar o status de candidaturas");
        }

        Long ownerUserId = application.getJobVacancy().getCompany().getUser().getId();
        if (!ownerUserId.equals(currentUser.getId())) {
            throw new BusinessException("Você não tem permissão para atualizar esta candidatura");
        }

        application.setStatus(newStatus);
        return toResponse(applicationRepository.save(application));
    }

    @Transactional
    public void deleteApplication(Long applicationId) {
        User currentUser = securityService.getCurrentUser();

        Application application = applicationRepository.findByIdAndNotDeleted(applicationId)
                .orElseThrow(() -> new BusinessException("Candidatura não encontrada"));

        if (currentUser.getRole() == Role.ADMIN) {
            application.setDeletedAt(LocalDateTime.now());
            applicationRepository.save(application);
            return;
        }

        if (currentUser.getRole() == Role.CANDIDATE) {
            if (!application.getCandidate().getId().equals(currentUser.getId())) {
                throw new BusinessException("Você não tem permissão para deletar esta candidatura");
            }
        } else if (currentUser.getRole() == Role.COMPANY) {
            Long ownerUserId = application.getJobVacancy().getCompany().getUser().getId();
            if (!ownerUserId.equals(currentUser.getId())) {
                throw new BusinessException("Você não tem permissão para deletar esta candidatura");
            }
        } else {
            throw new BusinessException("Você não tem permissão para deletar esta candidatura");
        }

        application.setDeletedAt(LocalDateTime.now());
        applicationRepository.save(application);
    }

    private ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                toUserResponse(application.getCandidate()),
                toJobSummary(application.getJobVacancy()),
                application.getCoverLetter(),
                application.getStatus(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }

    private UserResponse toUserResponse(User user) {
        if (user == null) return null;
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    private JobSummaryResponse toJobSummary(JobVacancy job) {
        if (job == null) return null;
        return new JobSummaryResponse(job.getId(), job.getTitle(), toCompanySummary(job.getCompany()));
    }

    private CompanySummaryResponse toCompanySummary(Company company) {
        if (company == null) return null;
        return new CompanySummaryResponse(company.getId(), company.getName(), company.getWebsite());
    }
}