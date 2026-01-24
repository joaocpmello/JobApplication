package com.jobs.jobboard.service;

import com.jobs.jobboard.dto.response.CompanySummaryResponse;
import com.jobs.jobboard.dto.response.JobResponse;
import com.jobs.jobboard.entity.*;
import com.jobs.jobboard.exception.BusinessException;
import com.jobs.jobboard.repository.CompanyRepository;
import com.jobs.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    public JobResponse createJob(String title, String description, String location, 
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

        return toResponse(jobRepository.save(job));
    }

    @Transactional(readOnly = true)
    public JobResponse getJobById(Long id) {
        JobVacancy job = jobRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("Vaga não encontrada com ID: " + id));
        return toResponse(job);
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAllNotDeleted().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<JobResponse> searchJobs(String title, Long companyId, String companyName, JobStatus status, Pageable pageable) {
        return jobRepository.searchJobs(
                normalize(title),
                companyId,
                normalize(companyName),
                status,
                pageable
        ).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobsByStatus(JobStatus status) {
        return jobRepository.findByStatusAndNotDeleted(status).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyIdAndNotDeleted(companyId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getMyCompanyJobs() {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem visualizar suas vagas");
        }

        Company company = companyRepository.findByUserIdAndNotDeleted(currentUser.getId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));

        return jobRepository.findByCompanyIdAndNotDeleted(company.getId()).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<JobResponse> getMyCompanyJobs(Pageable pageable) {
        User currentUser = securityService.getCurrentUser();

        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem visualizar suas vagas");
        }

        Company company = companyRepository.findByUserIdAndNotDeleted(currentUser.getId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));

        return jobRepository.findByCompanyIdAndNotDeleted(company.getId(), pageable).map(this::toResponse);
    }

    @Transactional
    public JobResponse updateJob(Long jobId, String title, String description, String location,
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

        return toResponse(jobRepository.save(job));
    }

    @Transactional
    public void deleteJob(Long jobId) {
        User currentUser = securityService.getCurrentUser();
        
        JobVacancy job = jobRepository.findByIdAndCompanyUserIdAndNotDeleted(jobId, currentUser.getId())
                .orElseThrow(() -> new BusinessException("Vaga não encontrada ou você não tem permissão para deletá-la"));

        job.setDeletedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private JobResponse toResponse(JobVacancy job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getStatus(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                toCompanySummary(job.getCompany()),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }

    private CompanySummaryResponse toCompanySummary(Company company) {
        if (company == null) return null;
        return new CompanySummaryResponse(company.getId(), company.getName(), company.getWebsite());
    }
}
