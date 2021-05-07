package com.example.demo.DTO;

import lombok.Data;

@Data
public class LoginResponse {
	public LoginResponse(String jwt1) {
		jwt = jwt1;
	}

	public String jwt;
}
