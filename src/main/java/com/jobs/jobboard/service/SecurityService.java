package com.jobs.jobboard.service;

import com.jobs.jobboard.config.security.JwtService;
import com.jobs.jobboard.entity.User;
import com.jobs.jobboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public SecurityService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));
        }
        
        throw new IllegalStateException("Tipo de autenticação não suportado");
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
