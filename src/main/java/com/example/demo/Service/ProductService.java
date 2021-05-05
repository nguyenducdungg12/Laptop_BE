package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.example.demo.DTO.ProductsResponse;
import com.example.demo.model.ProductModel;

public interface ProductService {
	ProductsResponse getAllProduct(int page);
	ProductModel addProduct(ProductModel product);
	Optional<ProductModel> findProductById(String id);
	List<ProductModel> getProductByCategory(String category, String Type, int page);
	List<ProductModel> getProductByTitle(String search);
}
	