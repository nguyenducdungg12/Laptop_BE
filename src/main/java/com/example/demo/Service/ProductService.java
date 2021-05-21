package com.example.demo.Service;

import java.util.Optional;

import com.example.demo.DTO.CommentResponse;
import com.example.demo.DTO.ProductsResponse;
import com.example.demo.model.CommentModel;
import com.example.demo.model.ProductModel;
import com.example.demo.model.ReplyModel;

public interface ProductService {
	ProductsResponse getAllProduct(int page, int sort, long max, long min);
	ProductModel addProduct(ProductModel product);
	Optional<ProductModel> findProductById(String id);
	ProductsResponse getProductByCategory(String category, int sort, long max, long min, String Type, int page);
	ProductsResponse getProductByTitle(String search,int page, int sort, long max, long min);
	ProductModel addComment(CommentModel commentModel, String id);
	CommentResponse getComment(String id, int page);
	ProductModel addReply(ReplyModel replyModel, String id, String idComment);
}
	