package com.RBU.Project2.security;

public class CustomUserDetails {
	private final Long userId;
	private final String email;
	
	public CustomUserDetails(Long userId, String email) {
		this.userId = userId;
		this.email = email;
	}
	public Long getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}
	
}
