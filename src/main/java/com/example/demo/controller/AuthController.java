package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		RegisterResponse registerResponse = new RegisterResponse();
		try {
			authService.registerUser(registerRequest);
			registerResponse.setStatusCode(200);
			registerResponse.setMsg("Tạo tài khoản thành công");
		}
		catch(Exception e) {
			registerResponse.setStatusCode(403);
			registerResponse.setMsg(e.getLocalizedMessage());
		}
		return registerResponse;
	}
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
		LoginResponse loginResponse = authService.Login(loginRequest);
		if(loginResponse.getStatusCode()!=200) {
			return new ResponseEntity<LoginResponse>(loginResponse,HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(loginResponse);
		
	}
	@GetMapping("/user")
	public ResponseEntity<?> getUser() {
		UserResponse userResponse = authService.getUserCurrent();
		if(userResponse.getStatusCode()!=200) {
			return new ResponseEntity<UserResponse>(userResponse,HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(userResponse);
	}
}
