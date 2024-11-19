package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payload.Response;
import com.example.payload.user.SignupUserRequest;
import com.example.payload.user.SignupUserResponse;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<Response<SignupUserResponse>> signup(@RequestBody SignupUserRequest request) {
		SignupUserResponse signupUserResponse = userService.createUser(request);
		
		return ResponseEntity.ok()
				.body(Response.success(signupUserResponse, "회원가입이 완료되었습니다."));
	}
	
}
