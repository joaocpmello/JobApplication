package com.jobs.jobboard.controller;

import com.jobs.jobboard.dto.request.CreateJobRequest;
import com.jobs.jobboard.dto.request.UpdateJobRequest;
import com.jobs.jobboard.entity.JobStatus;
import com.jobs.jobboard.entity.JobVacancy;
import com.jobs.jobboard.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobVacancy> createJob(@Valid @RequestBody CreateJobRequest request) {
        JobVacancy job = jobService.createJob(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getSalaryMin(),
                request.getSalaryMax()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobVacancy> getJobById(@PathVariable Long id) {
        JobVacancy job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<JobVacancy>> getAllJobs() {
        List<JobVacancy> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobVacancy>> getJobsByStatus(@PathVariable JobStatus status) {
        List<JobVacancy> jobs = jobService.getJobsByStatus(status);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobVacancy>> getMyCompanyJobs() {
        List<JobVacancy> jobs = jobService.getMyCompanyJobs();
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobVacancy> updateJob(@PathVariable Long id, 
                                                 @Valid @RequestBody UpdateJobRequest request) {
        JobVacancy job = jobService.updateJob(
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
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
