package com.jobs.jobboard.dto.response;

public class JobSummaryResponse {

    private Long id;
    private String title;
    private CompanySummaryResponse company;

    public JobSummaryResponse() {}

    public JobSummaryResponse(Long id, String title, CompanySummaryResponse company) {
        this.id = id;
        this.title = title;
        this.company = company;
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

    public CompanySummaryResponse getCompany() {
        return company;
    }

    public void setCompany(CompanySummaryResponse company) {
        this.company = company;
    }
}

