package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.UserRepo;
import com.example.demo.model.UserModel;

@RestController
public class UserController {
	@Autowired
	private UserRepo User;
	@GetMapping("/users")
	public ResponseEntity<?> getUser(){
		List<UserModel> ListUser = User.findAll();
		if(ListUser.size()>0) {
			return new ResponseEntity<>(ListUser,HttpStatus.OK);
		}
		return new ResponseEntity<>(ListUser,HttpStatus.OK);
	}
}
