package com.blogapp.blog.services;

import java.util.List;

import com.blogapp.blog.entities.Comment;
import com.blogapp.blog.payloads.CommentDto;

public interface CommentService {
	public CommentDto createComment(CommentDto commentDto,Integer postId,Integer userId);

	public CommentDto getCommentById(Integer id);
	
	public void deleteComment(Integer id);
	
	public List<CommentDto> getCommentByPost(Integer id);
	
	public List<CommentDto> getCommentByUser(Integer id);
}
