package com.jobs.jobboard.controller;

import com.jobs.jobboard.dto.request.CreateApplicationRequest;
import com.jobs.jobboard.entity.Application;
import com.jobs.jobboard.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Application> createApplication(@Valid @RequestBody CreateApplicationRequest request) {
        Application application = applicationService.createApplication(
                request.getJobId(),
                request.getCoverLetter()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<Application>> getMyApplications() {
        List<Application> applications = applicationService.getMyApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<Application>> getApplicationsByJob(@PathVariable Long jobId) {
        List<Application> applications = applicationService.getApplicationsByJob(jobId);
        return ResponseEntity.ok(applications);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'COMPANY')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
