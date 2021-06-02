package com.example.demo.DTO;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="Products")
public class ProductRequest {
		public String id;
		public String title;
		public double oldprice;
		public double newprice;
		public String category;
		public String content;
		public String type;
		public int quantity;
}
