package com.example.payload.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {	
	private String refreshToken;
}
