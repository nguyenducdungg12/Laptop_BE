package com.example.demo.DTO;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.model.ProductModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor

@Data
@Component
public class ProductsResponse {
	List<ProductModel> ListProducts;
	int TotalPage;
	int ProductPerPage;
	int TotalProduct;
}
