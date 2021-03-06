package com.example.demo.DTO;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
	public String jwt;
	public String msg;
	public int statusCode;
}
