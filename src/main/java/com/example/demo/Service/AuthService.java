package com.example.demo.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.ForgotPasswordResponse;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.OrderRequest;
import com.example.demo.DTO.OrderResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.UpdatedUserRequest;
import com.example.demo.DTO.UserResponse;
import com.example.demo.model.OrderModel;
import com.example.demo.model.UserModel;

public interface AuthService {
	LoginResponse Login(LoginRequest loginRequest);
	UserResponse getUserCurrent();
	void registerUser(RegisterRequest registerRequest) throws UnsupportedEncodingException, MessagingException;
	ForgotPasswordResponse forgotPassword(String email) throws UnsupportedEncodingException, MessagingException;
	void changePassword(String password,String email);
	OrderResponse addOrder(OrderRequest orderRequest);
	List<OrderModel> getOrder();
	OrderResponse deleteOrder(String id);
	OrderResponse cancelOrder(String id);
	OrderResponse updatedUser(UpdatedUserRequest updateUserRequest, MultipartFile multipartFile);
	UserModel userDetail();
	List<OrderModel> getAllOrder();
	OrderResponse acceptOrder(String id);
	void registerUserFB(RegisterRequest registerRequest);
}

