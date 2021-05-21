package com.example.demo.DTO;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.model.CommentModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class CommentResponse {
	List<CommentModel> ListComment;
	int TotalPage;
	int CommentPerPage;
	int TotalComment;
}
