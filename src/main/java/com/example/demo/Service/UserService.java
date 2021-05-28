package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.OrderModel;
import com.example.demo.model.UserModel;

public interface UserService {
	List<UserModel> findAll();
	UserDetails loadUserById(String userId);
	Optional<UserModel> findByVerificationcode(String verificationcode);
	void deleteUser(String id);
}
