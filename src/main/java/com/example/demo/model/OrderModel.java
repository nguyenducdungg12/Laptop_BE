package com.example.demo.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="Orders")
public class OrderModel {

	String id;
	List<OrderProduct> products;	
	String address;
	long TotalPrice;
	String payment;
	Date timeorder;
	String cancelreason;
	Boolean status_order;
	
}
