package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.ProductModel;

public interface ProductRepo extends MongoRepository<ProductModel, String> {
	List<ProductModel> findByCategoryAndType(String category,String type);
	@Query("{'title':{'$regex':'?0','$options':'i'}}")
	List<ProductModel> findByTitle(String search);
}
