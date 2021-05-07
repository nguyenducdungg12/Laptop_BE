package com.example.demo.Service;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.UserResponse;

public interface AuthService {
	LoginResponse Login(LoginRequest loginRequest);
	UserResponse getUserCurrent();
}
