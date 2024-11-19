package com.example.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthResponse {

	private String grantType;
	private String accessToken;
	private String refreshtoken;
}
