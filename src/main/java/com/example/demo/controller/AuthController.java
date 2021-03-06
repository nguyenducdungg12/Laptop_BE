package com.example.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.ForgotPasswordRequest;
import com.example.demo.DTO.ForgotPasswordResponse;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.OrderRequest;
import com.example.demo.DTO.OrderResponse;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.RegisterResponse;
import com.example.demo.DTO.UpdatedUserRequest;
import com.example.demo.DTO.UserResponse;
import com.example.demo.Exception.CustomException;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.UserService;
import com.example.demo.model.OrderModel;
import com.example.demo.model.UserModel;
import com.example.demo.security.JwtTokenProvider;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	@Autowired
	UserService userService;
	@Autowired
	AuthService authService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepo userRepo;
	@Autowired
    JwtTokenProvider tokenProvider;

	@PostMapping("/register")
	public RegisterResponse registerUser(@RequestBody RegisterRequest registerRequest){
		RegisterResponse registerResponse = new RegisterResponse();
		try {
			authService.registerUser(registerRequest);
			registerResponse.setStatusCode(200);
			registerResponse.setMsg("T???o t??i kho???n th??nh c??ng");
		}
		catch(Exception e) {
			registerResponse.setStatusCode(403);
			registerResponse.setMsg(e.getLocalizedMessage());
		}
		return registerResponse;
	}
	@PostMapping("/register/facebook")
	public RegisterResponse registerUserFB(@RequestBody RegisterRequest registerRequest){
		RegisterResponse registerResponse = new RegisterResponse();
		try {
			authService.registerUserFB(registerRequest);
			registerResponse.setStatusCode(200);
			registerResponse.setMsg("????ng nh???p th??nh c??ng");
		}
		catch(Exception e) {
			registerResponse.setStatusCode(403);
			registerResponse.setMsg(e.getLocalizedMessage());
		}
		return registerResponse;
	}
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
		LoginResponse loginResponse = authService.Login(loginRequest);
		return ResponseEntity.ok(loginResponse);
		
	}
	@PostMapping("/forgot")
	public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest request) throws UnsupportedEncodingException, MessagingException{
		return authService.forgotPassword(request.getEmail());
		
	}
	@GetMapping("/forgot/{token}")
	public ResponseEntity<?> checkTokenForget(@PathVariable("token") String token) {
		ForgotPasswordResponse response = new ForgotPasswordResponse();
		if(tokenProvider.validateToken(token)) {
			response.setStatus(200);
		}
		else {
			response.setStatus(404);
		}
		return ResponseEntity.ok(response);
}
	@PostMapping("/forgot/{token}")
	public ResponseEntity<?> forgotPasswordChange(@PathVariable("token") String token,@RequestBody ForgotPasswordRequest request ) {


		ForgotPasswordResponse response = new ForgotPasswordResponse();
			if(tokenProvider.validateToken(token)) {
				String emailUser = tokenProvider.getUserIdFromJWT(token);
				authService.changePassword(request.getPassword(), emailUser);
				response.setStatus(200);
			}
			else {
				response.setStatus(404);

			}
		return ResponseEntity.ok(response);
	}
	@PostMapping("/order")
	public OrderResponse addOrder(@RequestBody OrderRequest orderRequest){
		return authService.addOrder(orderRequest);	
	}
	@PostMapping("/order/cancel")
	public ResponseEntity<?> cancelOrder(@RequestBody OrderModel ordermodel){
		UserResponse currentUser = authService.getUserCurrent();
		if(currentUser.getRole().equals("ADMIN")||currentUser.getId().equals(ordermodel.getIdUser())) {			
			return ResponseEntity.ok(authService.cancelOrder(ordermodel.getId()));	
		}
		else {
			return ResponseEntity.status(404).body("Kh??ng c?? quy???n truy c???p");
		}
	}
	@PostMapping("/order/accept")
	public ResponseEntity<?> acceptOrder(@RequestBody OrderModel ordermodel){
		UserResponse currentUser = authService.getUserCurrent();
		if(currentUser.getRole().equals("ADMIN")) {			
			return ResponseEntity.ok(authService.acceptOrder(ordermodel.getId()));	
		}
		else {
			return ResponseEntity.status(404).body("Kh??ng c?? quy???n truy c???p");

		}
	}
	@GetMapping("/order")
	public List<OrderModel> getOrder(){
		return authService.getOrder();
	}
	@DeleteMapping("/order")
	public OrderResponse deleteOrder(@RequestBody OrderModel ordermodel){
		return authService.deleteOrder(ordermodel.getId());
	}
	@GetMapping("/orders")
	public ResponseEntity<?> getAllOrder(){
		UserResponse currentUser = authService.getUserCurrent();
		if(currentUser.getRole().equals("ADMIN")) {			
			return ResponseEntity.ok(authService.getAllOrder());
		}
		else {
			return ResponseEntity.status(404).body("Kh??ng c?? quy???n truy c???p");
		}

	}
	@PostMapping("/user/update")
	public OrderResponse updateUser(@ModelAttribute UpdatedUserRequest updateUserRequest,@RequestParam(value="image",required = false) MultipartFile multipartFile) throws IOException{		
		return authService.updatedUser(updateUserRequest,multipartFile);
	}
	@GetMapping("/user")
	public ResponseEntity<?> getUser() {
		UserResponse userResponse = authService.getUserCurrent();
		return ResponseEntity.ok(userResponse);
	}
	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		UserResponse currentUser = authService.getUserCurrent();
		if(currentUser.getRole().equals("ADMIN")) {			
			return ResponseEntity.ok(userService.findAll());
		}
		else {
			return ResponseEntity.status(404).body("Kh??ng c?? quy???n truy c???p");
		}
	}
	@DeleteMapping("/user")
	public ResponseEntity<?> deleteUser(@RequestBody String id){
		UserResponse currentUser = authService.getUserCurrent();
		if(currentUser.getRole().equals("ADMIN")) {			

			try {
				userService.deleteUser(id);
				return ResponseEntity.ok("Th??nh c??ng");		
			}
			catch(Exception err) {
				return ResponseEntity.status(404).body(err);
			}
		}
		else {
			return ResponseEntity.status(404).body("Kh??ng c?? quy???n truy c???p");
		}
	}
	@GetMapping("/accountVerification/{vertificationcode}")
	public ResponseEntity<?> vertificationUser(@PathVariable("vertificationcode") String vertificationcode){
		Optional<UserModel> userOption = userService.findByVerificationcode(vertificationcode);
		if(userOption.isPresent()) {
			UserModel user= userOption.orElse(null);
			user.setEnable(true);
			userRepo.save(user);
			return ResponseEntity.ok("<head> <meta charset=\"utf-8\" /></head><body><h1 style='text-align:center'>K??ch ho???t th??nh c??ng<h1></body>");
		}
		else {	
			return ResponseEntity.ok("K??ch ho???t th???t b???i");
		}
		
	}

}
