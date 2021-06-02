package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.OrderModel;

public interface OrderRepo extends MongoRepository<OrderModel, String> {
	@Query("{'idUser' : ?0}")
	List<OrderModel> findByIduser(String idUser);
}
