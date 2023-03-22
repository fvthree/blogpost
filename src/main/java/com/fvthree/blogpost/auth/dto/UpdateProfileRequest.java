package com.fvthree.blogpost.auth.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
	
	private Long userId;
	
	private String name;
	
	private String password;
	
	private String address;
}
