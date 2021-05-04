package com.example.demo.Service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.UserService;
import com.example.demo.model.UserModel;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepo Users;
	@Override
	public List<UserModel> findAll() {
		return Users.findAll();
	}

}
