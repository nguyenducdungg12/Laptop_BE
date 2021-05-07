package com.example.demo.Service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.UserResponse;
import com.example.demo.Exception.CustomException;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.AuthService;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.UserModel;
import com.example.demo.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService {

		@Autowired
	    AuthenticationManager authenticationManager;	
	
		@Autowired
	    private JwtTokenProvider tokenProvider;
	    
		@Autowired
		PasswordEncoder passwordEncoder;
	    
		@Autowired
		UserRepo UserRepo;
	public LoginResponse Login(LoginRequest loginRequest) {
		Authentication authentication;
		LoginResponse loginResponse =new LoginResponse();
		try {	
			 authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsername(),
							loginRequest.getPassword()
							)
					); 
		}
		catch(Exception e) {
			loginResponse.setStatusCode(403);
			loginResponse.setMsg("Mật khẩu hoặc tài khoản không đúng");
			return loginResponse;
		}
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = tokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());    
	        loginResponse.setStatusCode(200);
	        loginResponse.setJwt(jwt);
	        loginResponse.setMsg("Đăng nhập thành công");
	       return loginResponse;
	}
	@Override
	public UserResponse getUserCurrent() {
		UserResponse userResponse = new UserResponse();
		try {
			CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
			userResponse.setEmail(currentUser.getUser().getEmail());
			userResponse.setImage(currentUser.getUser().getImage());
			userResponse.setPhone(currentUser.getUser().getPhone());
			userResponse.setRole(currentUser.getUser().getRole());
			userResponse.setUsername(currentUser.getUsername());
			userResponse.setStatusCode(200);
			return userResponse;
		}
		catch(Exception e) {
			userResponse.setStatusCode(404);
			return userResponse;
		}
	}
	public void registerUser(RegisterRequest registerRequest) {
		  Optional<UserModel> isUserValid  = UserRepo.findByUsername(registerRequest.getUsername());
		  if(isUserValid.isPresent()) {
	            throw new CustomException("Tài khoản đã được sử dụng");
		  }
		  if (registerRequest.getUsername().length() < 6 || registerRequest.getPassword().length() < 6) {
	            throw new CustomException("Tài khoản và mật khẩu phải nhiều hơn 6 ký tự");
	        }
		UserModel newUser = new UserModel();
		newUser.setUsername(registerRequest.getUsername());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
