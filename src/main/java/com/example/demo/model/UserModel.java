package com.example.demo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

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
}
