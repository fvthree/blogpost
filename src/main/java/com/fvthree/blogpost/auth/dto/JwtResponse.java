package com.fvthree.blogpost.auth.dto;

import lombok.Data;

@Data
public class JwtResponse {

	private final String roles;

	private String accessToken;

	private String refreshToken;

	private String tokenType = "Bearer";

	private String email;


	public JwtResponse(String accessToken, String refreshToken, String email, String roles) {
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.email = email;
		this.roles = roles;
	}
}
