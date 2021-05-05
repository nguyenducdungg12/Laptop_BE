package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ProductsResponse;
import com.example.demo.Service.ProductService;
import com.example.demo.model.ProductModel;
@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping("/api/products")
	public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "1",name="page") int page,@RequestParam(defaultValue = "",name="search") String search){
		ProductsResponse listProduct;
		if(!search.equals("")) {
			listProduct = productService.getProductByTitle(search, page);
		}
		else {			
			listProduct = productService.getAllProduct(page);
		}
		return ResponseEntity.ok(listProduct);
		
	}
	@GetMapping("/api/products/{category}")
	public ResponseEntity<?> getProductByCategory(@PathVariable("category") String category , @RequestParam(defaultValue = "1",name="page") int page,@RequestParam(defaultValue="1",name="type") String type){
		ProductsResponse listProduct = productService.getProductByCategory(category, type, page);
		return ResponseEntity.ok(listProduct);

	}
	@GetMapping("/api/product/{id}")
	public ResponseEntity<?> getProductById(@PathVariable("id") String id){
		Optional<ProductModel> Product = productService.findProductById(id);
		if(Product.isPresent()) {
			return new ResponseEntity<Optional<ProductModel>>(Product,HttpStatus.OK);
		}
		else {			
			return new ResponseEntity<String>("Khong co san pham nao",HttpStatus.NOT_FOUND); 
		}
	}
	@PostMapping("/api/product")
	public ResponseEntity<?> postProduct(@RequestBody ProductModel product){
		System.out.print(product);
		try {
			productService.addProduct(product);			
		}
		catch(Exception err) {
			return ResponseEntity.status(404).body(err);
		}
		return ResponseEntity.ok(product);
	}
}
