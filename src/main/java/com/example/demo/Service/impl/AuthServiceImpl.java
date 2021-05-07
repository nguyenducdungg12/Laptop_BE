package com.example.demo.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.UserResponse;
import com.example.demo.Service.AuthService;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService {

		@Autowired
	    AuthenticationManager authenticationManager;	
	
		@Autowired
	    private JwtTokenProvider tokenProvider;
	    
	    
	public LoginResponse Login(LoginRequest loginRequest) {
		Authentication authentication;
		try {	
			 authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							loginRequest.getPassword()
							)
					); 
		}
		catch(Exception e) {
			return new LoginResponse("Loi"+e);
		}
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = tokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());    
	       return new LoginResponse(jwt);
	}
	@Override
	public UserResponse getUserCurrent() {
		UserResponse userResponse = new UserResponse();
		CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(123);
		System.out.println(currentUser);
		userResponse.setEmail(currentUser.getUser().getEmail());
		userResponse.setImage(currentUser.getUser().getImage());
		userResponse.setPhone(currentUser.getUser().getPhone());
		userResponse.setRole(currentUser.getUser().getRole());
		userResponse.setUsername(currentUser.getUsername());
		return userResponse;
	}

}
