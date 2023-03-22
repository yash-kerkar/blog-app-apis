package com.blogapp.blog.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.blog.entities.Comment;
import com.blogapp.blog.payloads.CommentDto;
import com.blogapp.blog.services.impl.AuthServiceImpl;
import com.blogapp.blog.services.impl.CommentServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
	
	@Autowired
	private CommentServiceImpl commentService;
	
	private AuthServiceImpl authService;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/user/{userId}/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto comment,@PathVariable Integer userId,
			@PathVariable Integer postId) {
		CommentDto commentDto = this.commentService.createComment(comment,postId,userId);
		return new ResponseEntity<>(commentDto,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Integer id,HttpServletRequest request){
		this.authService.isCommentByUser(id,request );
		Map<String, String> response = new HashMap<>();
		this.commentService.deleteComment(id);
		response.put("Message", "Comment deleted sucessfully");
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.OK);
	}
	
	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Integer postId){
		List<CommentDto> comments = this.commentService.getCommentByPost(postId);
		return new ResponseEntity<List<CommentDto>>(comments,HttpStatus.OK);
	}
	
}
