package com.jobs.jobboard.service;

import com.jobs.jobboard.dto.response.CompanyResponse;
import com.jobs.jobboard.entity.Company;
import com.jobs.jobboard.entity.Role;
import com.jobs.jobboard.entity.User;
import com.jobs.jobboard.exception.BusinessException;
import com.jobs.jobboard.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final SecurityService securityService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, SecurityService securityService) {
        this.companyRepository = companyRepository;
        this.securityService = securityService;
    }

    @Transactional
    public CompanyResponse createCompany(String name, String description, String cnpj, String website) {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas usuários com perfil de empresa podem criar uma empresa");
        }

        if (companyRepository.findByUserIdAndNotDeleted(currentUser.getId()).isPresent()) {
            throw new BusinessException("Você já possui uma empresa cadastrada");
        }

        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        company.setCnpj(cnpj);
        company.setWebsite(website);
        company.setUser(currentUser);

        return toResponse(companyRepository.save(company));
    }

    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));
        return toResponse(company);
    }

    public CompanyResponse getMyCompany() {
        User currentUser = securityService.getCurrentUser();
        
        if (currentUser.getRole() != Role.COMPANY) {
            throw new BusinessException("Apenas empresas podem visualizar seus dados");
        }

        Company company = companyRepository.findByUserIdAndNotDeleted(currentUser.getId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));
        return toResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompany(String name, String description, String cnpj, String website) {
        User currentUser = securityService.getCurrentUser();
        
        Company company = companyRepository.findByUserIdAndNotDeleted(currentUser.getId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));

        if (!company.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Você não tem permissão para editar esta empresa");
        }

        if (name != null) company.setName(name);
        if (description != null) company.setDescription(description);
        if (cnpj != null) company.setCnpj(cnpj);
        if (website != null) company.setWebsite(website);

        return toResponse(companyRepository.save(company));
    }

    @Transactional
    public void deleteCompany(Long companyId) {
        User currentUser = securityService.getCurrentUser();
        
        Company company = companyRepository.findByIdAndNotDeleted(companyId)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));

        if (!company.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Você não tem permissão para deletar esta empresa");
        }

        company.setDeletedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    private CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getWebsite(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
