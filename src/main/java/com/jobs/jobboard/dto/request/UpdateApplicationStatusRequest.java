package com.jobs.jobboard.dto.request;

import com.jobs.jobboard.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateApplicationStatusRequest {

    @NotNull(message = "Status é obrigatório")
    private ApplicationStatus status;

    public UpdateApplicationStatusRequest() {}

    public UpdateApplicationStatusRequest(ApplicationStatus status) {
        this.status = status;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}