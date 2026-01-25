package com.jobs.jobboard.dto.response;

import com.jobs.jobboard.entity.ApplicationStatus;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private UserResponse candidate;
    private JobSummaryResponse job;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApplicationResponse() {}

    public ApplicationResponse(
            Long id,
            UserResponse candidate,
            JobSummaryResponse job,
            String coverLetter,
            ApplicationStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.candidate = candidate;
        this.job = job;
        this.coverLetter = coverLetter;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getCandidate() {
        return candidate;
    }

    public void setCandidate(UserResponse candidate) {
        this.candidate = candidate;
    }

    public JobSummaryResponse getJob() {
        return job;
    }

    public void setJob(JobSummaryResponse job) {
        this.job = job;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
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