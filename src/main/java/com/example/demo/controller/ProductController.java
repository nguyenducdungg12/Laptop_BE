package com.example.demo.controller;

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

import com.example.demo.DTO.CommentResponse;
import com.example.demo.DTO.ProductsResponse;
import com.example.demo.Service.ProductService;
import com.example.demo.model.CommentModel;
import com.example.demo.model.ProductModel;
import com.example.demo.model.ReplyModel;
@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	
	@GetMapping("/api/products")
	public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "1",name="page") int page,@RequestParam(defaultValue = "0",name="sort") int sort,@RequestParam(defaultValue = "0",name="max") long max,@RequestParam(defaultValue = "0",name="min",required = false) long min,@RequestParam(defaultValue = "",name="search") String search){
		ProductsResponse listProduct;
		if(!search.equals("")) {
			listProduct = productService.getProductByTitle(search, page,sort,max,min);
		}
		else {			
			listProduct = productService.getAllProduct(page,sort,max,min);
		}
		return ResponseEntity.ok(listProduct);
	}
	@GetMapping("/api/products/{category}")
	public ResponseEntity<?> getProductByCategory(@PathVariable("category") String category ,@RequestParam(defaultValue = "0",name="sort") int sort,@RequestParam(defaultValue = "0",name="max") long max,@RequestParam(defaultValue = "0",name="min",required = false) long min, @RequestParam(defaultValue = "1",name="page") int page,@RequestParam(defaultValue="1",name="type") String type){
		ProductsResponse listProduct = productService.getProductByCategory(category,sort,max,min, type, page);
		return ResponseEntity.ok(listProduct);

	}
	@GetMapping("/api/detailproducts/{id}")
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
		try {
			productService.addProduct(product);			
		}
		catch(Exception err) {
			return ResponseEntity.status(404).body(err);
		}
		return ResponseEntity.ok(product);
	}
	@PostMapping("/api/detailproducts/{id}/comment")
	public ResponseEntity<?> postComment(@PathVariable("id") String id,@RequestBody CommentModel commentModel){
		ProductModel productModel = productService.addComment(commentModel,id);
		return ResponseEntity.ok(productModel);
	}
	@PostMapping("/api/detailproducts/{idProduct}/reply/{idComment}")
	public ResponseEntity<?> postReply(@PathVariable("idProduct") String id,@PathVariable("idComment") String idComment,@RequestBody ReplyModel replyModel){
		ProductModel productModel = productService.addReply(replyModel,id,idComment);
		return ResponseEntity.ok(productModel);
	}
	@GetMapping("/api/detailproducts/{id}/comment")
	public ResponseEntity<?> getComment(@PathVariable("id") String id,@RequestParam(defaultValue = "1",name="page") int page){
		CommentResponse productModel = productService.getComment(id,page);
		return ResponseEntity.ok(productModel);
	}

}
