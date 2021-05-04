package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.ProductModel;

public interface ProductRepo extends MongoRepository<ProductModel, String> {
	List<ProductModel> findByCategoryAndType(String category,String type);
}
