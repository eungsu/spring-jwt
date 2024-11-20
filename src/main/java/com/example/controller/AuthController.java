package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payload.Response;
import com.example.payload.auth.AuthRequest;
import com.example.payload.auth.AuthResponse;
import com.example.payload.auth.RefreshTokenRequest;
import com.example.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<Response<AuthResponse>> login(@RequestBody AuthRequest request) {
		AuthResponse data = authService.login(request);
		
		return ResponseEntity.ok()
			.body(Response.success(data, "로그인이 완료되었습니다."));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<Response<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
		AuthResponse data = authService.refreshToken(request);
        
        return ResponseEntity.ok()
			.body(Response.success(data, "엑세스토큰이 재발급되었습니다."));
	}
}
