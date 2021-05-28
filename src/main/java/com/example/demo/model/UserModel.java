package com.example.demo.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Users")
public class UserModel {
	@Id
	public String id;
	@Field("createdBy")
	public Date createBy;
	@Field("updatedBy")
	public Date updatedBy;
	@Field(value = "username")
	public String username;
	@Field(value = "password")
	public String password;
	@Field(value = "phone")
	public String phone;
	@Field(value = "email")
	public String email;
	@Field(value="image")
	public String image;
	@Field(value="listimage")
	public List <String> listimage;
	@Field(value="role")
	public String role;
	@Field(value="enable")
	Boolean enable;
	@Field(value="verificationcode")
	String verificationcode;
	@Field(value="forgotpassword")
	String forgotpassword;
	@Field(value="ngaysinh")
    public String ngaysinh;
	@Field(value="sex")
    public String sex;
	@Field(value="order")
	List<OrderModel> orders;
}
