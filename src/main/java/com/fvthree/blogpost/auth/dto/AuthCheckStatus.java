package com.fvthree.blogpost.auth.dto;

public class AuthCheckStatus {
	
	private String status;
	
	public AuthCheckStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
