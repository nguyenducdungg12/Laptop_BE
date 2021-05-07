package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.RegisterResponse;
import com.example.demo.DTO.UserResponse;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.UserService;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	@Autowired
	UserService userService;
	@Autowired
	AuthService authService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@PostMapping("/register")
	public RegisterResponse registerUser(@RequestBody RegisterRequest registerRequest){
		registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		RegisterResponse registerResponse = new RegisterResponse();
		try {
			userService.registerUser(registerRequest);
			registerResponse.setStatusCode(200);
			registerResponse.setMsg("Tạo tài khoản thành công");
		}
		catch(Exception e) {
			registerResponse.setStatusCode(403);
			registerResponse.setMsg("Lỗi : "+e);
		}
		return registerResponse;
	}
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest){
		return authService.Login(loginRequest);
	}
	@GetMapping("/user")
	public UserResponse getUser() {
		return authService.getUserCurrent();
	}
}
