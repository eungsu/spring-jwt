package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.payload.Response;
import com.example.payload.auth.AuthRequest;
import com.example.payload.auth.AuthResponse;
import com.example.payload.auth.RefreshTokenRequest;
import com.example.security.JwtUtil;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<Response<AuthResponse>> login(@RequestBody AuthRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		String accessToken = jwtUtil.generateAccessToken(authentication.getName());
		String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());
		
		userService.saveRefreshToken(authentication.getName(), refreshToken);
		
		AuthResponse data = AuthResponse.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.refreshtoken(refreshToken)
				.build();
		
		return ResponseEntity.ok()
				.body(Response.success(data, "로그인이 완료되었습니다."));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<Response<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
		String refreshToken = request.getRefreshToken();
		if (!jwtUtil.validateRefreshToken(refreshToken)) {
			throw new IllegalArgumentException("유효한 리프레시 토큰이 아닙니다.");
		}
        String username = jwtUtil.extractUsernameInRefreshToken(refreshToken);
    
        User user = userService.getUser(username);    
        if (!user.getRefreshToken().equals(refreshToken)) {
        	throw new IllegalArgumentException("유효한 리프레시 토큰이 아닙니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(username);
        AuthResponse data = AuthResponse.builder()
				.grantType("Bearer")
				.accessToken(accessToken)
				.build();
        
        return ResponseEntity.ok()
				.body(Response.success(data, "엑세스토큰이 재발급되었습니다."));
	}
}
