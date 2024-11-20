package com.example.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.payload.auth.AuthRequest;
import com.example.payload.auth.AuthResponse;
import com.example.payload.auth.RefreshTokenRequest;
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
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
		String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        AuthResponse authResponse = AuthResponse.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshtoken(refreshToken)
				.build();

        return authResponse;
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
		if (!jwtUtil.validateRefreshToken(refreshToken)) {
			throw new IllegalArgumentException("유효한 리프레시 토큰이 아닙니다.");
		}
        Long userId = jwtUtil.extractUserIdInRefreshToken(refreshToken);
    
        User user = userRepository.findById(userId).get();    
        if (!user.getRefreshToken().equals(refreshToken)) {
        	throw new IllegalArgumentException("유효한 리프레시 토큰이 아닙니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        AuthResponse authResponse = AuthResponse.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.build();

        return authResponse;
    }
}
