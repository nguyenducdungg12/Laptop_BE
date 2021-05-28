package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.OrderModel;

public interface OrderRepo extends MongoRepository<OrderModel, String> {

}
