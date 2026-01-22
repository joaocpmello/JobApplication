package com.jobs.jobboard.controller;

import com.jobs.jobboard.dto.request.CreateCompanyRequest;
import com.jobs.jobboard.entity.Company;
import com.jobs.jobboard.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        Company company = companyService.createCompany(
                request.getName(),
                request.getDescription(),
                request.getCnpj(),
                request.getWebsite()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/my-company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Company> getMyCompany() {
        Company company = companyService.getMyCompany();
        return ResponseEntity.ok(company);
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CreateCompanyRequest request) {
        Company company = companyService.updateCompany(
                request.getName(),
                request.getDescription(),
                request.getCnpj(),
                request.getWebsite()
        );
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
