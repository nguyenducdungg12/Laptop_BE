package com.example.demo.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderProduct {
	public long soluong;
	public String id;
	public Date createBy;
	public Date updatedBy; 
	public String title;
	public double oldprice;
	public double newprice;
	public String image;
	public String category;
	public String content;
	public int type;
	public List<String> listImage;	
	
}
