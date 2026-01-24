package com.jobs.jobboard.dto.response;

import com.jobs.jobboard.entity.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private JobStatus status;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private CompanySummaryResponse company;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobResponse() {}

    public JobResponse(
            Long id,
            String title,
            String description,
            String location,
            JobStatus status,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            CompanySummaryResponse company,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.status = status;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.company = company;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public CompanySummaryResponse getCompany() {
        return company;
    }

    public void setCompany(CompanySummaryResponse company) {
        this.company = company;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

