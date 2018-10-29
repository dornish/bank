package com.scbb.bank.authentication.payload;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

	private String token;
	private String tokenType;

	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}

}
