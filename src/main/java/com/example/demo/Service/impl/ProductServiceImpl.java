package com.example.demo.Service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ProductsResponse;
import com.example.demo.Repository.ProductRepo;
import com.example.demo.Service.ProductService;
import com.example.demo.model.ProductModel;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepo Products;
	@Autowired
	ProductsResponse productResponse;
	@Override
	public ProductsResponse getAllProduct(int page) {
		List<ProductModel> ListProduct= Products.findAll();
		if(page==1) {
			if(ListProduct.size()<page*15) {
				productResponse.setListProducts(ListProduct);
			}
			else {
				
				productResponse.setListProducts(ListProduct.subList(0, page*15));
			}			
		}
		else {
			if(ListProduct.size()<page*15) {
				if((page+1)*3-ListProduct.size()>15) {
					productResponse.setListProducts(Collections.emptyList());
				}
				else {
					productResponse.setListProducts(ListProduct.subList((page-1)*15, ListProduct.size()));
				}
			}
			productResponse.setListProducts(ListProduct.subList((page-1)*15, page*15));
		}
		productResponse.setTotalProduct(ListProduct.size());
		productResponse.setTotalPage((productResponse.getTotalProduct()/15)+1);
		productResponse.setProductPerPage(15);
		return productResponse;
		
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
	@Override
	public List<ProductModel> getProductByCategory(String category, String Type, int page) {
			List<ProductModel> ListProduct= Products.findByCategoryAndType(category,Type);
			if(page==1) {
				
				if(ListProduct.size()<page*15) {
					productResponse.setListProducts(ListProduct);
				}
				return ListProduct.subList(0, page*15);
			}
			else {
				if(ListProduct.size()<page*15) {
					if((page+1)*15-ListProduct.size()>15) {
						return Collections.emptyList();
					}
					else {
						return  ListProduct.subList((page-1)*15, ListProduct.size());
					}
				}
				return ListProduct.subList((page-1)*15, page*15);
			}
	}
	public List<ProductModel> getProductByTitle(String search) {
		return Products.findByTitle(search);
	}


}
