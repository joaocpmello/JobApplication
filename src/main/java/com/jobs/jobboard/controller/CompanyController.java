package com.jobs.jobboard.controller;

import com.jobs.jobboard.dto.request.CreateCompanyRequest;
import com.jobs.jobboard.dto.response.CompanyResponse;
import com.jobs.jobboard.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "Company profile endpoints")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Create a company profile for the current user (company only)")
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        CompanyResponse company = companyService.createCompany(
                request.getName(),
                request.getDescription(),
                request.getCnpj(),
                request.getWebsite()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a company by id")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        CompanyResponse company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/my-company")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Get the current user's company profile")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        CompanyResponse company = companyService.getMyCompany();
        return ResponseEntity.ok(company);
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Update the current user's company profile")
    public ResponseEntity<CompanyResponse> updateCompany(@Valid @RequestBody CreateCompanyRequest request) {
        CompanyResponse company = companyService.updateCompany(
                request.getName(),
                request.getDescription(),
                request.getCnpj(),
                request.getWebsite()
        );
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Delete a company profile (soft delete, must own it)")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
