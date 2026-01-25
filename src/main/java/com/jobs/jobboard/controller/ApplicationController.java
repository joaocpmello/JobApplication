package com.jobs.jobboard.controller;

import com.jobs.jobboard.dto.request.CreateApplicationRequest;
import com.jobs.jobboard.dto.request.UpdateApplicationStatusRequest;
import com.jobs.jobboard.dto.response.ApplicationResponse;
import com.jobs.jobboard.entity.ApplicationStatus;
import com.jobs.jobboard.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Job application endpoints")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Apply to a job vacancy (candidate only)")
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody CreateApplicationRequest request) {
        ApplicationResponse application = applicationService.createApplication(
                request.getJobId(),
                request.getCoverLetter()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'COMPANY', 'ADMIN')")
    @Operation(summary = "Get an application by id")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long id) {
        ApplicationResponse application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "List applications from the current candidate")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        List<ApplicationResponse> applications = applicationService.getMyApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "List applications for a job vacancy with pagination (company only, must own the job)")
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsByJob(
            @PathVariable Long jobId,
            @RequestParam(required = false) ApplicationStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ApplicationResponse> applications = applicationService.getApplicationsByJob(jobId, status, pageable);
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Update application status (company only, must own the job)")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        ApplicationResponse application = applicationService.updateApplicationStatus(id, request.getStatus());
        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'COMPANY', 'ADMIN')")
    @Operation(summary = "Delete an application (soft delete)")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}