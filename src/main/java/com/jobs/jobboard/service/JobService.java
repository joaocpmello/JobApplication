package com.jobs.jobboard.service;

import com.jobs.jobboard.entity.*;
import com.jobs.jobboard.exception.BusinessException;
import com.jobs.jobboard.repository.CompanyRepository;
import com.jobs.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SecurityService securityService;

    @Autowired
    public JobService(JobRepository jobRepository, CompanyRepository companyRepository, SecurityService securityService) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.securityService = securityService;
    }

    @Transactional
    public JobVacancy createJob(String title, String description, String location, 
                                 BigDecimal salaryMin, BigDecimal salaryMax) {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem criar vagas");
        }

        Company company = companyRepository.findByUserIdAndNotDeleted(currentUser.getId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada para o usuário"));

        JobVacancy job = new JobVacancy();
        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setSalaryMin(salaryMin);
        job.setSalaryMax(salaryMax);
        job.setStatus(JobStatus.OPEN);
        job.setCompany(company);

        return jobRepository.save(job);
    }

    public JobVacancy getJobById(Long id) {
        return jobRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("Vaga não encontrada com ID: " + id));
    }

    public List<JobVacancy> getAllJobs() {
        return jobRepository.findAllNotDeleted();
    }

    public List<JobVacancy> getJobsByStatus(JobStatus status) {
        return jobRepository.findByStatusAndNotDeleted(status);
    }

    public List<JobVacancy> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyIdAndNotDeleted(companyId);
    }

    public List<JobVacancy> getMyCompanyJobs() {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem visualizar suas vagas");
        }

        Company company = companyRepository.findByUserIdAndNotDeleted(currentUser.getId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));

        return jobRepository.findByCompanyIdAndNotDeleted(company.getId());
    }

    @Transactional
    public JobVacancy updateJob(Long jobId, String title, String description, String location,
                                 BigDecimal salaryMin, BigDecimal salaryMax, JobStatus status) {
        User currentUser = securityService.getCurrentUser();
        
        JobVacancy job = jobRepository.findByIdAndCompanyUserIdAndNotDeleted(jobId, currentUser.getId())
                .orElseThrow(() -> new BusinessException("Vaga não encontrada ou você não tem permissão para editá-la"));

        if (title != null) job.setTitle(title);
        if (description != null) job.setDescription(description);
        if (location != null) job.setLocation(location);
        if (salaryMin != null) job.setSalaryMin(salaryMin);
        if (salaryMax != null) job.setSalaryMax(salaryMax);
        if (status != null) job.setStatus(status);

        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long jobId) {
        User currentUser = securityService.getCurrentUser();
        
        JobVacancy job = jobRepository.findByIdAndCompanyUserIdAndNotDeleted(jobId, currentUser.getId())
                .orElseThrow(() -> new BusinessException("Vaga não encontrada ou você não tem permissão para deletá-la"));

        job.setDeletedAt(LocalDateTime.now());
        jobRepository.save(job);
    }
}
