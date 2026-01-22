package com.jobs.jobboard.dto.request;

import com.jobs.jobboard.entity.JobStatus;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class UpdateJobRequest {

    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    private String title;

    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String description;

    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private JobStatus status;

    public UpdateJobRequest() {}

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

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
}
