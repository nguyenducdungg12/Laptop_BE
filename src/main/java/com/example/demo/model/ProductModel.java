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
@Document(collection="Products")
public class ProductModel  {
	@Id
	public String id;
	@Field("createdBy")
	public Date createBy;
	@Field("updatedBy")
	public Date updatedBy; 
	@Field(value="title")
	public String title;
	@Field(value="oldprice")
	public double oldprice;
	@Field(value="newprice")
	public double newprice;
	@Field(value="image")
	public String image;
	@Field(value="category")
	public String category;
	@Field(value="content")
	public String content;
	@Field(value="type")
	public String type;
	@Field(value="ListImage")
	List<String> ListImage;
	@Field(value="comment")
	List<CommentModel> comment;
}
