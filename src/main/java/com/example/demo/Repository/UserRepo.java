package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserModel;
@Repository
public interface UserRepo extends MongoRepository<UserModel, String> {

	  Optional<UserModel> findByUsername(String username);
}
