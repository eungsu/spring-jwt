package com.example.payload.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignupUserResponse {	
	private Long id;
	private String username;
	private String email;
	private String nickname;
}
