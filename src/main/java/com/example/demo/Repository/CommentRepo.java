package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.CommentModel;

public interface CommentRepo extends MongoRepository<CommentModel, String>{

}
