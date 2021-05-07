package com.example.demo.DTO;

import java.util.Date;

import com.example.demo.model.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {

	public String username;
	public String phone;
	public String email;
    public String image;
    private String role;
}
