package com.blogapp.blog.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.blogapp.blog.payloads.PostDto;
import com.blogapp.blog.payloads.PostResponse;

public interface PostService {
	PostDto createPost(PostDto post, Integer userId, Integer categoryId);
	
	PostDto updatePost(PostDto post,Integer postId);
	
	PostDto getPostId(Integer postId);
	
	PostResponse getAllPosts(int pageSize,int pageNumber, String sortBy,String sortDir);
	
	void deletePost(Integer postId);
	
	PostResponse getAllPostByCategory(Integer postId,int pageSize,int pageNumber,String sortBy,String sortDir);
	
	PostResponse getAllPostByUser(Integer userId,int pageSize,int pageNumber,String sortBy,String sortDir);
	
	PostResponse searchPost(String keyword,int pageSize,int pageNumber,String sortBy,String sortDir);
}
