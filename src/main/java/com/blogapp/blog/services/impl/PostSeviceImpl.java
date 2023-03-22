package com.blogapp.blog.services.impl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blogapp.blog.entities.Category;
import com.blogapp.blog.entities.Post;
import com.blogapp.blog.entities.User;
import com.blogapp.blog.exceptions.ResourceNotFoundException;
import com.blogapp.blog.payloads.PostDto;
import com.blogapp.blog.payloads.PostResponse;
import com.blogapp.blog.repositories.CategoryRepository;
import com.blogapp.blog.repositories.PostRepository;
import com.blogapp.blog.repositories.UserRepository;
import com.blogapp.blog.services.PostService;

@Service
public class PostSeviceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		
		User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		Post postCreated = this.postRepo.save(post);
		return this.modelMapper.map(postCreated,PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		int id = postDto.getCategory().getId();
		Category category = this.categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","id", id));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		post.setCategory(category);
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost,PostDto.class);
	}

	@Override
	public PostDto getPostId(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		return this.modelMapper.map(post,PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(int pageSize,int pageNumber, String sortBy,String sortDir) {
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("ASC")) sort = Sort.by(sortBy).ascending();
		else sort = Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageSize, pageNumber,sort );
		Page<Post> posts1 = this.postRepo.findAll(p);
		List<Post> posts = posts1.getContent();
		PostResponse reponse = new PostResponse();
		List<PostDto> postDto = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		reponse.setContent(postDto);
		reponse.setPageNumber(posts1.getNumber());
		reponse.setPageSize(posts1.getSize());
		reponse.setTotalElements(posts1.getTotalElements());
		reponse.setTotalPages(posts1.getTotalPages());
		reponse.setLastPage(posts1.isLast());
		
		return reponse;
	}

	@Override
	public void deletePost(Integer postId) {
		this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		this.postRepo.deleteById(postId);
	}

	@Override
	public PostResponse getAllPostByCategory(Integer CategoryId,int pageSize,int pageNumber,String sortBy,String sortDir) {
		Category category = this.categoryRepository.findById(CategoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id", CategoryId));
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("ASC")) sort = Sort.by(sortBy).ascending();
		else sort = Sort.by(sortBy).descending();
		System.out.println(sortBy+" "+sortDir);
		Pageable p = PageRequest.of(pageSize, pageNumber,sort);
		Page<Post> posts1 = this.postRepo.findByCategory(category,p);
		List<Post> posts = posts1.getContent();
		List<PostDto> postDto = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse reponse = new PostResponse();
		reponse.setContent(postDto);
		reponse.setPageNumber(posts1.getNumber());
		reponse.setPageSize(posts1.getSize());
		reponse.setTotalElements(posts1.getTotalElements());
		reponse.setTotalPages(posts1.getTotalPages());
		reponse.setLastPage(posts1.isLast());
		
		return reponse;
	}

	@Override
	public PostResponse getAllPostByUser(Integer userId,int pageSize,int pageNumber,String sortBy,String sortDir) {
		User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("ASC")) sort = Sort.by(sortBy).ascending();
		else sort = Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageSize, pageNumber,sort);
		Page<Post> posts1 = this.postRepo.findByUser(user,p);
		List<Post> posts = posts1.getContent();
		List<PostDto> postDto = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse reponse = new PostResponse();
		reponse.setContent(postDto);
		reponse.setPageNumber(posts1.getNumber());
		reponse.setPageSize(posts1.getSize());
		reponse.setTotalElements(posts1.getTotalElements());
		reponse.setTotalPages(posts1.getTotalPages());
		reponse.setLastPage(posts1.isLast());
		
		return reponse;
	}

	@Override
	public PostResponse searchPost(String keyword,int pageSize,int pageNumber,String sortBy,String sortDir) {
		
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("ASC")) sort = Sort.by(sortBy).ascending();
		else sort = Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageNumber,pageSize,sort);
		Page<Post> posts1 = this.postRepo.findByTitleContaining(keyword, p);
		List<Post> posts = posts1.getContent();
		List<PostDto> postDto = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse reponse = new PostResponse();
		reponse.setContent(postDto);
		reponse.setPageNumber(posts1.getNumber());
		reponse.setPageSize(posts1.getSize());
		reponse.setTotalElements(posts1.getTotalElements());
		reponse.setTotalPages(posts1.getTotalPages());
		reponse.setLastPage(posts1.isLast());
		
		return reponse;
	}

}
