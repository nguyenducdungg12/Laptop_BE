package com.example.demo.Service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.example.demo.Repository.OrderRepo;
import com.example.demo.Repository.ProductRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.MailService;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.OrderModel;
import com.example.demo.model.OrderProduct;
import com.example.demo.model.ProductModel;
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
		OrderRepo orderRepo;
		@Autowired
		UserRepo UserRepo;
		@Autowired
		ProductRepo Products;
		@Autowired
		MailService mailService;
		
		public LoginResponse Login(LoginRequest loginRequest) {
		Authentication authentication;
		LoginResponse loginResponse =new LoginResponse();
		System.out.println(loginRequest);
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
			loginResponse.setMsg("M???t kh???u ho???c t??i kho???n kh??ng ????ng");
			return loginResponse;
		}
			UserModel checkUser = UserRepo.findByUsername(authentication.getName()).orElse(null);
			if(!checkUser.getEnable()) {
				loginResponse.setStatusCode(404);
				loginResponse.setMsg("T??i kho???n ch??a ???????c k??ch ho???t vui l??ng ????ng nh???p email ????? k??ch ho???t");
				 
			}
			else {				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = tokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());    
				loginResponse.setStatusCode(200);
				loginResponse.setJwt(jwt);
				loginResponse.setMsg("????ng nh???p th??nh c??ng");
			}
	       return loginResponse;
	}
	@Override
	public UserResponse getUserCurrent() {
		UserResponse userResponse = new UserResponse();
		try {
			CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
			Optional<UserModel> user= UserRepo.findByUsername(currentUser.getUsername());	
			userResponse.setEmail(user.get().getEmail());
			userResponse.setImage(user.get().getImage());
			userResponse.setPhone(user.get().getPhone());
			userResponse.setRole(user.get().getRole());
			userResponse.setUsername(user.get().getUsername());
			userResponse.setName(user.get().getName());
			userResponse.setNgaysinh(user.get().getNgaysinh());
			userResponse.setSex(user.get().getSex());
			userResponse.setStatusCode(200);
			userResponse.setId(user.get().getId());
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
	            throw new CustomException("T??i kho???n ???? ???????c s??? d???ng");
		  }
		  if(isEmailValid.isPresent()) {
	            throw new CustomException("Email ???? ???????c s??? d???ng");
		  }
		  if (registerRequest.getUsername().length() < 6 || registerRequest.getPassword().length() < 6) {
	            throw new CustomException("T??i kho???n v?? m???t kh???u ph???i nhi???u h??n 6 k?? t???");
	      }
		UserModel newUser = new UserModel();
		newUser.setUsername(registerRequest.getUsername());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		newUser.setName(registerRequest.getUsername());
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
	public void registerUserFB(RegisterRequest registerRequest) {
		  Optional<UserModel> isEmailValid  = UserRepo.findByEmail(registerRequest.getEmail());
		  if(isEmailValid.isPresent()) {
			  if(isEmailValid.get().getUsername().equals(registerRequest.getUsername())) {
				  return;
			  }
			  else {
				  throw new CustomException("Email ???? ???????c s??? d???ng");

			  }
		  }else {
			UserModel newUser = new UserModel();
			newUser.setUsername(registerRequest.getUsername());
			newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			newUser.setName(registerRequest.getName());
			newUser.setEmail(registerRequest.getEmail());
			newUser.setRole("USER");
			newUser.setCreateBy(new Date());
			newUser.setUpdatedBy(new Date());
			newUser.setImage(registerRequest.getImage());
			newUser.setEnable(true);
			UserRepo.save(newUser);
			}
	}
	@Override
	public ForgotPasswordResponse forgotPassword(String email) throws UnsupportedEncodingException, MessagingException {
		ForgotPasswordResponse response = new ForgotPasswordResponse();
		 Optional<UserModel> user = UserRepo.findByEmail(email);
		 if(user.isPresent()) {
			 user.get().setForgotpassword(tokenProvider.generateTokenForgotPassword(email));		
			 mailService.sendForgotPassword(user.get(),"https://localhost:3000");
			 UserRepo.save(user.get());
			 response.setStatus(200);
			 response.setMsg("Vui l??ng ????ng nh???p v??o gmail ????? thay ?????i password");
		 }else {
			 response.setStatus(404);
			 response.setMsg("T??i kho???n email ch??a ???????c ????ng k??");
		
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
			orderModel.setTotalPrice(getTotalPrice(orderRequest.getProducts()));
			orderModel.setIdUser(user.get().id);
			orderRepo.save(orderModel);
			response.setMsg("?????t h??ng th??nh c??ng");
			response.setStatusCode(200);
		}
		else {
			response.setMsg("C?? l???i trong qu?? tr??nh ?????t h??ng");
			response.setStatusCode(404);
		}
		return response;
	}
	@Override
	public OrderModel addOrderCheck(OrderRequest orderRequest) {
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			OrderModel orderModel = new OrderModel();
			orderModel.setAddress(orderRequest.getAdress());
			orderModel.setPayment(orderRequest.getPayment());
			orderModel.setProducts(orderRequest.getProducts());;
			orderModel.setTimeorder(new Date());
			orderModel.setStatus_order(false);	
			orderModel.setTotalPrice(getTotalPrice(orderRequest.getProducts()));
			orderModel.setIdUser(user.get().id);
			return orderRepo.save(orderModel);
			
		}
		return null;
	}
	@Override
	public List<OrderModel> getOrder() {
		UserResponse userReponse = getUserCurrent();	
		return orderRepo.findByIduser(userReponse.getId());
	}
	@Override
	public OrderResponse deleteOrder(String id) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			orderRepo.deleteById(id);
			response.setStatusCode(200);
			response.setMsg("X??a ????n h??ng th??nh c??ng");
		}
		else {
			response.setStatusCode(404);
			response.setMsg("X??a ????n h??ng th???t b???i");
		}
		return response;
	}
	@Override
	public OrderResponse cancelOrder(String id) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			OrderModel temp = orderRepo.findById(id).get();
			temp.setCancelreason("0");
			orderRepo.save(temp);
			response.setStatusCode(200);
			response.setMsg("???? H???y ????n h??ng");
		}
		else {
			response.setStatusCode(404);
			response.setMsg("H???y ????n h??ng th???t b???i");
		}
		return response;
	}
	@Override
	public OrderResponse acceptOrder(String id) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			OrderModel temp = orderRepo.findById(id).get();
			for(int i=0 ;i<temp.getProducts().size();i++) {
				OrderProduct orderProduct = temp.getProducts().get(i);
				ProductModel modelProduct = Products.findById(orderProduct.getId()).get();
				modelProduct.setQuantity(modelProduct.getQuantity()-orderProduct.getSoluong());
				Products.save(modelProduct);
				
			}
			temp.setStatus_order(true);
			orderRepo.save(temp);
			response.setStatusCode(200);
			response.setMsg("Duy???t th??nh c??ng ????n h??ng");
		}
		else {
			response.setStatusCode(404);
			response.setMsg("Duy???t ????n h??ng th???t b???i");
		}
		return response;
	}
	@Override
	public OrderResponse updatedUser(UpdatedUserRequest updateUserRequest,MultipartFile multipartFile) {
		OrderResponse response = new OrderResponse();
		UserResponse userReponse = getUserCurrent();
		Optional<UserModel> user= UserRepo.findByUsername(userReponse.getUsername());
		if(user.isPresent()) {
			user.get().setPhone(updateUserRequest.getPhone());
			user.get().setSex(updateUserRequest.getSex());
			user.get().setNgaysinh(updateUserRequest.getNgaysinh());
			user.get().setName(updateUserRequest.getName());
			if(multipartFile!=null) {					
				String fileName = multipartFile.getOriginalFilename();
		        Path uploadPath = Paths.get("src/main/resources/static/image");
				try {
					InputStream inputStream = multipartFile.getInputStream();
		            Path filePath = uploadPath.resolve(fileName);
		            Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					 new CustomException(e.getLocalizedMessage());
				}
				user.get().setImage("http://localhost:8080/image/"+fileName);
			}
			if(!updateUserRequest.getCurrentPassword().equals("")&&passwordEncoder.matches(updateUserRequest.getCurrentPassword(),user.get().getPassword())) {
				user.get().setPassword(passwordEncoder.encode(updateUserRequest.getNewPassword()));
				UserRepo.save(user.get());
				response.setStatusCode(200);
				response.setMsg("C???p nh???t th??nh c??ng");
			}
			else if(!updateUserRequest.getCurrentPassword().equals("")&&!passwordEncoder.matches(updateUserRequest.getCurrentPassword(),user.get().getPassword())){
				response.setStatusCode(404);
				response.setMsg("M???t kh???u hi???n t???i kh??ng kh???p ");
				return response;
 
			}
			else {
				UserRepo.save(user.get());
				response.setStatusCode(200);
				response.setMsg("C???p nh???t th??nh c??ng");
			}
		}		
		else {
			response.setStatusCode(404);
			response.setMsg("C?? l???i x???y ra ");
		}
		return response;
	}
	@Override
	public UserModel userDetail() {
		return UserRepo.findByEmail(getUserCurrent().getEmail()).get();
	}
	@Override
	public List<OrderModel> getAllOrder() {
		return orderRepo.findAll();
	}


	
	
	
	
		
		
}
