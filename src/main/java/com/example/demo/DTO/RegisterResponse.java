package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor

@Data
public class RegisterResponse {
	public long statusCode;
	public String msg;
	
}
