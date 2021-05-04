package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.ProductModel;

public interface ProductRepo extends MongoRepository<ProductModel, String> {
	
}
