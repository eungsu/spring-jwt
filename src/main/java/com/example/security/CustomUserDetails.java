package com.example.security;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.model.User;

public class CustomUserDetails extends LoginUser implements UserDetails {
	private static final long serialVersionUID = 1L;

	public CustomUserDetails(User user) {
		super(user);
	}
}
