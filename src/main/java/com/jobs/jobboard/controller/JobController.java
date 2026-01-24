package com.jobs.jobboard.controller;

import com.jobs.jobboard.dto.request.CreateJobRequest;
import com.jobs.jobboard.dto.request.UpdateJobRequest;
import com.jobs.jobboard.dto.response.JobResponse;
import com.jobs.jobboard.entity.JobStatus;
import com.jobs.jobboard.service.JobService;
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
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "Job vacancies management (create, search, update, delete)")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Create a job vacancy (company only)")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        JobResponse job = jobService.createJob(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getSalaryMin(),
                request.getSalaryMax()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a job vacancy by id")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        JobResponse job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    @Operation(summary = "Search job vacancies with pagination, filters and sorting")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long companyId,
            @RequestParam(name = "company", required = false) String companyName,
            @RequestParam(required = false) JobStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<JobResponse> jobs = jobService.searchJobs(title, companyId, companyName, status, pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/all")
    @Operation(summary = "List all job vacancies (no pagination)")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "List job vacancies by status (no pagination)")
    public ResponseEntity<List<JobResponse>> getJobsByStatus(@PathVariable JobStatus status) {
        List<JobResponse> jobs = jobService.getJobsByStatus(status);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "List jobs from the current company (pagination supported)")
    public ResponseEntity<Page<JobResponse>> getMyCompanyJobs(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(jobService.getMyCompanyJobs(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Update a job vacancy (company only, must own the job)")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, 
                                                 @Valid @RequestBody UpdateJobRequest request) {
        JobResponse job = jobService.updateJob(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getSalaryMin(),
                request.getSalaryMax(),
                request.getStatus()
        );
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Delete a job vacancy (soft delete, company only, must own the job)")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
