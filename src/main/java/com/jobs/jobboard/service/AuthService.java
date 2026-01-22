package com.jobs.jobboard.service;

import com.jobs.jobboard.config.security.JwtService;
import com.jobs.jobboard.dto.request.LoginRequest;
import com.jobs.jobboard.dto.response.LoginResponse;
import com.jobs.jobboard.dto.response.UserResponse;
import com.jobs.jobboard.entity.User;
import com.jobs.jobboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        UserResponse userResponse = convertToResponse(user);

        return new LoginResponse(token, userResponse);
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
