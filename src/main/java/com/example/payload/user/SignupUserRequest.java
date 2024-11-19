package com.example.payload.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignupUserRequest {

	private String username;
	private String password;
	private String email;
	private String nickname;
}
