package com.example.demo.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.Service.AuthService;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.security.JwtTokenProvider;

public class AuthServiceImpl implements AuthService {

	 @Autowired
	    AuthenticationManager authenticationManager;

	    @Autowired
	    private JwtTokenProvider tokenProvider;
	public LoginResponse Login(LoginRequest loginRequest) {
		  Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginRequest.getUsername(),
	                        loginRequest.getPassword()
	                )
	        );
		  
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = tokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());    
	       return new LoginResponse(jwt);
	}

}
