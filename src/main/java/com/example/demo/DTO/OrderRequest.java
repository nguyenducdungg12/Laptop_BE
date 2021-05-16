package com.example.demo.DTO;

import java.util.List;

import com.example.demo.model.OrderProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequest {
	String adress;
	String payment;	
	List<OrderProduct> products;
}
