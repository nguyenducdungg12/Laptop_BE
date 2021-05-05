package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.ProductModel;

public interface ProductRepo extends MongoRepository<ProductModel, String> {
	@Query("{'category' : ?0 , 'type' : ?1}")
	List<ProductModel> findByCategoryAndType(String category,String type);
	@Query("{'category':{'$ne':'?0'}}")
	List<ProductModel> findByCategoryAndTypePK(String category,String type);
	@Query("{'title':{'$regex':'?0','$options':'i'}}")
	List<ProductModel> findByTitle(String search);
}
