package com.example.demo.DTO;

import lombok.Data;
@Data
public class RegisterRequest {
	public String username;
	public String password;
	public String phone;
	public String email;
}
