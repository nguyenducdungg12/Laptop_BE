package com.example.demo.Service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.ForgotPasswordResponse;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.OrderRequest;
import com.example.demo.DTO.OrderResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.UpdatedUserRequest;
import com.example.demo.DTO.UserResponse;
import com.example.demo.Exception.CustomException;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.MailService;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.OrderModel;
import com.example.demo.model.OrderProduct;
import com.example.demo.model.UserModel;
import com.example.demo.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService {

		@Autowired
	    AuthenticationManager authenticationManager;	
	
		@Autowired
	    JwtTokenProvider tokenProvider;
	    
		@Autowired
		PasswordEncoder passwordEncoder;
	    
		@Autowired
		UserRepo UserRepo;
		
		@Autowired
		MailService mailService;
		
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
			UserModel checkUser = UserRepo.findByUsername(authentication.getName()).orElse(null);
			if(!checkUser.getEnable()) {
				loginResponse.setStatusCode(404);
				loginResponse.setMsg("Tài khoản chưa được kích hoạt vui lòng đăng nhập email để kích hoạt");
				 
			}
			else {				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = tokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());    
				loginResponse.setStatusCode(200);
				loginResponse.setJwt(jwt);
				loginResponse.setMsg("Đăng nhập thành công");
			}
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
	public void registerUser(RegisterRequest registerRequest) throws UnsupportedEncodingException, MessagingException{
		  Optional<UserModel> isUserNameValid  = UserRepo.findByUsername(registerRequest.getUsername());
		  Optional<UserModel> isEmailValid  = UserRepo.findByEmail(registerRequest.getEmail());
		  if(isUserNameValid.isPresent()) {
	            throw new CustomException("Tài khoản đã được sử dụng");
		  }
		  if(isEmailValid.isPresent()) {
	            throw new CustomException("Email đã được sử dụng");
		  }
		  if (registerRequest.getUsername().length() < 6 || registerRequest.getPassword().length() < 6) {
	            throw new CustomException("Tài khoản và mật khẩu phải nhiều hơn 6 ký tự");
	      }
		UserModel newUser = new UserModel();
		newUser.setUsername(registerRequest.getUsername());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		newUser.setPhone(registerRequest.getPhone());
		newUser.setEmail(registerRequest.getEmail());
		newUser.setRole("USER");
		newUser.setCreateBy(new Date());
		newUser.setUpdatedBy(new Date());
		newUser.setImage("https://cdn.shortpixel.ai/client/q_glossy,ret_img,w_632,h_316/https://gocsuckhoe.com/wp-content/uploads/2020/09/avatar-facebook-632x316.jpg");
		String randomCode = UUID.randomUUID().toString();
		newUser.setVerificationcode(randomCode);
		newUser.setEnable(false);
		UserRepo.save(newUser);
		mailService.sendVerificationEmail(newUser,"http://localhost:8080");
	}
	@Override
	public ForgotPasswordResponse forgotPassword(String email) throws UnsupportedEncodingException, MessagingException {
		ForgotPasswordResponse response = new ForgotPasswordResponse();
		 Optional<UserModel> user = UserRepo.findByEmail(email);
		 if(user.isPresent()) {
			 user.get().setForgotpassword(tokenProvider.generateTokenForgotPassword(email));		
			 mailService.sendForgotPassword(user.get(),"http://localhost:3000");
			 UserRepo.save(user.get());
			 response.setStatus(200);
			 response.setMsg("Vui lòng đăng nhập vào gmail để thay đổi password");
		 }else {
			 response.setStatus(404);
			 response.setMsg("Tài khoản email chưa được đăng ký");
		
		 }
		 return response;
		 
	}
	@Override
	public void changePassword(String password, String email) {
		  Optional<UserModel> user  = UserRepo.findByEmail(email);
		  if(user.isPresent()) {
			  user.get().setPassword(passwordEncoder.encode(password));
			  UserRepo.save(user.get());
		  }
	
	}
	public long getTotalPrice(List<OrderProduct> ListProduct) {
		long total =0;
		for(int i=0;i<ListProduct.size();i++) {
			total+=ListProduct.get(i).soluong*ListProduct.get(i).newprice;
		}
		return total;
	}
	@Override
	public OrderResponse addOrder(OrderRequest orderRequest) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			OrderModel orderModel = new OrderModel();
			orderModel.setAddress(orderRequest.getAdress());
			orderModel.setPayment(orderRequest.getPayment());
			orderModel.setProducts(orderRequest.getProducts());;
			orderModel.setTimeorder(new Date());
			orderModel.setStatus_order(false);
			List <OrderModel> order;	
			if(user.get().getOrders()==null) {
				order = new ArrayList<OrderModel>();
			}
			else {
				order = user.get().getOrders();
			}
			orderModel.setId(UUID.randomUUID().toString());
			orderModel.setTotalPrice(getTotalPrice(orderRequest.getProducts()));
			order.add(orderModel);
			
			user.get().setOrders(order);
			UserRepo.save(user.get());
			response.setMsg("Đặt hàng thành công");
			response.setStatusCode(200);
		}
		else {
			response.setMsg("Có lỗi trong quá trình đặt hàng");
			response.setStatusCode(404);
		}
		return response;
	}
	@Override
	public List<OrderModel> getOrder() {
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());		
		return user.get().getOrders();
	}
	@Override
	public OrderResponse deleteOrder(String id) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			List <OrderModel>order = user.get().getOrders();
			for(int i=0;i<order.size();i++) {
				if(order.get(i).getId().equals(id)) {
					order.remove(i);
					user.get().setOrders(order);
					UserRepo.save(user.get());
					break;
				}
			}
			response.setStatusCode(200);
			response.setMsg("Xóa đơn hàng thành công");
		}
		else {
			response.setStatusCode(404);
			response.setMsg("Xóa đơn hàng thất bại");
		}
		return response;
	}
	@Override
	public OrderResponse cancelOrder(String id) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			List <OrderModel>order = user.get().getOrders();
			for(int i=0;i<order.size();i++) {
				if(order.get(i).getId().equals(id)) {
					order.get(i).setCancelreason("0");
					user.get().setOrders(order);
					UserRepo.save(user.get());
					break;
				}
			}
			response.setStatusCode(200);
			response.setMsg("Đã Hủy đơn hàng");
		}
		else {
			response.setStatusCode(404);
			response.setMsg("Hủy đơn hàng thất bại");
		}
		return response;
	}
	@Override
	public OrderResponse updatedUser(UpdatedUserRequest updateUserRequest,MultipartFile multipartFile) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			user.get().setSex(updateUserRequest.getSex());
			user.get().setNgaysinh(updateUserRequest.getNgaysinh());
			if(multipartFile!=null) {					
				String fileName = multipartFile.getOriginalFilename();
		        Path uploadPath = Paths.get("dsadsadadsa");
				try {
		            System.out.println(uploadPath);
					InputStream inputStream = multipartFile.getInputStream();
		            Path filePath = uploadPath.resolve(fileName);
		            System.out.println(filePath);
		            Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					 new CustomException(e.getLocalizedMessage());
				}
				user.get().setImage("http://localhost:8080/image/"+fileName);
			}
			if(!updateUserRequest.getCurrentPassword().equals("")&&user.get().getPassword().equals(passwordEncoder.encode(updateUserRequest.getCurrentPassword()))) {
				user.get().setPassword(passwordEncoder.encode(updateUserRequest.getNewPassword()));
			}
			else {
				response.setStatusCode(404);
				response.setMsg("Mật khẩu hiện tại không khớp ");
			}
				UserRepo.save(user.get());
				response.setStatusCode(200);
				response.setMsg("Cập nhật thành công");
		}		
		else {
			response.setStatusCode(404);
			response.setMsg("Có lỗi xảy ra ");
		}
		return response;
	}
	
	
	
		
		
}
