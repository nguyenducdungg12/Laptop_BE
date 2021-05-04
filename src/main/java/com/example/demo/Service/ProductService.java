package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.example.demo.model.ProductModel;

public interface ProductService {
	List<ProductModel> getAllProduct();
	ProductModel addProduct(ProductModel product);
	Optional<ProductModel> findProductById(String id);
	List<ProductModel> getProductByCategory(String category, String Type, int page);
}
