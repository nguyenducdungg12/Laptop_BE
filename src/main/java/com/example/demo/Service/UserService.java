package com.example.demo.Service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.DTO.RegisterRequest;
import com.example.demo.model.UserModel;

public interface UserService {
	List<UserModel> findAll();
	UserDetails loadUserById(String userId);
}
