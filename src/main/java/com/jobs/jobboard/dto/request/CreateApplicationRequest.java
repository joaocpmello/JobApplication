package com.jobs.jobboard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateApplicationRequest {

    @NotNull(message = "ID da vaga é obrigatório")
    private Long jobId;

    @Size(max = 1000, message = "Carta de apresentação deve ter no máximo 1000 caracteres")
    private String coverLetter;

    public CreateApplicationRequest() {}

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}
