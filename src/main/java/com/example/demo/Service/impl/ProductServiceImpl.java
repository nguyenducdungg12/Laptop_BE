package com.example.demo.Service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.ProductRepo;
import com.example.demo.Service.ProductService;
import com.example.demo.model.ProductModel;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepo Products;
	@Override
	public List<ProductModel> getAllProduct() {
		return Products.findAll();
	}
	public ProductModel addProduct(ProductModel product) {
		product.setCreateBy(new Date());
		product.setUpdatedBy(new Date());
		return Products.save(product);
	}
	@Override
	public Optional<ProductModel> findProductById(String id) {
		return Products.findById(id);
	}

}
