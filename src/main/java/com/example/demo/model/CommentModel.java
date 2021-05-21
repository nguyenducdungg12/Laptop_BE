package com.example.demo.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.demo.DTO.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {
	@Id
	public String id;
	UserResponse user;
	String content;
	int start;
	@Field("createdBy")
	public Date createBy;
	List<ReplyModel> reply;
}
