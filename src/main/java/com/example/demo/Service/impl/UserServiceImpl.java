package com.example.demo.Service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.RegisterRequest;
import com.example.demo.Exception.CustomException;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.UserService;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.UserModel;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {

	@Autowired
	UserRepo UserRepo;
	
	@Override
	public List<UserModel> findAll() {
		return UserRepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserModel> UserOption = UserRepo.findByUsername(username);
		
		UserModel User = UserOption.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username : " + username));
		return new CustomUserDetails(User);
	}

	@Override
	public UserDetails loadUserById(String userId) {
	Optional<UserModel> UserOption = UserRepo.findById(userId);
		UserModel User = UserOption.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with id : " + userId));
		return new CustomUserDetails(User);
	}

	@Override
	public void registerUser(RegisterRequest registerRequest) {
		  Optional<UserModel> isUserValid  = UserRepo.findByUsername(registerRequest.getUsername());
		  if(isUserValid.isPresent()) {
	            throw new CustomException("Username is aldready use");
		  }
		  if (registerRequest.getUsername().length() < 5 || registerRequest.getPassword().length() < 5) {
	            throw new CustomException("Email and password must bigger than 5");
	        }
		UserModel newUser = new UserModel();
		newUser.setUsername(registerRequest.getUsername());
		newUser.setPassword(registerRequest.getPassword());
		newUser.setPhone(registerRequest.getPhone());
		newUser.setEmail(registerRequest.getEmail());
		newUser.setRole("USER");
		newUser.setCreateBy(new Date());
		newUser.setUpdatedBy(new Date());
		newUser.setImage("https://cdn.shortpixel.ai/client/q_glossy,ret_img,w_632,h_316/https://gocsuckhoe.com/wp-content/uploads/2020/09/avatar-facebook-632x316.jpg");
		UserRepo.save(newUser);
	}
	
}
