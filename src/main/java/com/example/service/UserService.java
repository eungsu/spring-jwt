package com.example.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.ERole;
import com.example.model.User;
import com.example.payload.user.SignupUserRequest;
import com.example.payload.user.SignupUserResponse;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public SignupUserResponse createUser(SignupUserRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("사용할 수 없는 사용자이름입니다.");
		}
		if (userRepository.existsByEmail(request.getEmail())) {			
			throw new IllegalArgumentException("사용할 수 없는 이메일입니다.");
		}
		
		User user = modelMapper.map(request, User.class);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(ERole.ROLE_USER);
		
		userRepository.save(user);
		
		return modelMapper.map(user, SignupUserResponse.class);
	}
}
