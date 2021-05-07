package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.RegisterRequest;
import com.example.demo.Service.UserService;

@RestController
public class AuthController {
	@Autowired
	UserService userService;
	
	@PostMapping("api/auth/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
		userService.registerUser(registerRequest);
	}
//	@GetMapping("api/auth/user/${id}")
//	public ResponseEntity<?> getUser(){
//		List<UserModel> ListUser = User.findAll();
//		if(ListUser.size()>0) {
//			return new ResponseEntity<>(ListUser,HttpStatus.OK);
//		}
//		return new ResponseEntity<>(ListUser,HttpStatus.OK);
//	}
}
