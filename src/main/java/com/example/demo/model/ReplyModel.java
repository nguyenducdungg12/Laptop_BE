package com.example.demo.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import com.example.demo.DTO.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyModel {
	UserResponse user;
	String content;
	@Field("createdBy")
	public Date createBy;

}
