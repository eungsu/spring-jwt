package com.example.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exception.JwtException;
import com.example.model.User;
import com.example.payload.auth.AuthRequest;
import com.example.payload.auth.AuthResponse;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {    
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new JwtException(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());

        AuthResponse authResponse = AuthResponse.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.build();

        return authResponse;
    }

}
