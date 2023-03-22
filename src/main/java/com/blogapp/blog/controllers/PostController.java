package com.blogapp.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogapp.blog.payloads.APIResponse;
import com.blogapp.blog.payloads.PostDto;
import com.blogapp.blog.payloads.PostResponse;
import com.blogapp.blog.services.impl.AuthServiceImpl;
import com.blogapp.blog.services.impl.FileServiceImpl;
import com.blogapp.blog.services.impl.PostSeviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PostController {
	
	@Autowired
	private PostSeviceImpl postService;
	
	@Autowired
	private FileServiceImpl fileService;
	
	@Autowired
	private AuthServiceImpl authService;
	
	@Value("${project.image}")
	private String path;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/users/{userId}/categories/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto posDto,@PathVariable Integer userId,@PathVariable Integer categoryId){
		return new ResponseEntity<>(this.postService.createPost(posDto, userId, categoryId),HttpStatus.CREATED);
	}
	

	@GetMapping("/users/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "5",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "Date",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "desc",required = false) String sortDir){
		return new ResponseEntity<>(this.postService.getAllPostByUser(userId,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
	}
	
	@GetMapping("/categories/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "5",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "Date",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "desc",required = false) String sortDir){
		return new ResponseEntity<>(this.postService.getAllPostByCategory(categoryId,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "5",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "Date",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "desc",required = false) String sortDir
			){
		return new ResponseEntity<>(this.postService.getAllPosts(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
	}
	
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
		return new ResponseEntity<>(this.postService.getPostId(postId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<APIResponse> deletePost(@PathVariable Integer postId,HttpServletRequest request){
		this.authService.isPostByUser(postId, request);
		this.postService.deletePost(postId);
		APIResponse response = new APIResponse("message",true);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId
			,HttpServletRequest request) {
		this.authService.isPostByUser(postId, request);
		return new ResponseEntity<>(this.postService.updatePost(postDto, postId),HttpStatus.OK);
	}
	
	@GetMapping("/posts/search/{name}")
	public ResponseEntity<PostResponse> getPostsByTitle(@PathVariable String name,
			@RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "5",required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = "Date",required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = "desc",required = false) String sortDir){
		System.out.println(name);
		return new ResponseEntity<>(this.postService.searchPost(name, pageSize, pageNumber, sortBy,sortDir),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadImage(@RequestParam MultipartFile image,@PathVariable Integer postId
			,HttpServletRequest request) throws IOException{
		this.authService.isPostByUser(postId, request);
		PostDto post = this.postService.getPostId(postId);
		String  name = this.fileService.uploadImage(path, image);
		System.out.println(name);
		post.setImageName(name);
		PostDto updatedPostDto = this.postService.updatePost(post, postId);
		return new ResponseEntity<>(updatedPostDto,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/post/image/replace/{postId}")
	public ResponseEntity<PostDto> replaceImage(@RequestParam MultipartFile image,@PathVariable Integer postId
			,HttpServletRequest request) throws IOException{
		this.authService.isPostByUser(postId, request);
		PostDto post = this.postService.getPostId(postId);
		String  name = this.fileService.replaceImage(path, image,post.getImageName());
		System.out.println(name);
		post.setImageName(name);
		PostDto updatedPostDto = this.postService.updatePost(post, postId);
		return new ResponseEntity<>(updatedPostDto,HttpStatus.OK);
	}
	
	@GetMapping("/post/image/{fileName}")
	public void downloadImage(@PathVariable String fileName,HttpServletResponse response) throws IOException {
		try {
			InputStream ip = this.fileService.getResource(path, fileName);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(ip,response.getOutputStream());
		}catch(Exception e) {
			response.sendError(500,"File not found");
		}
	}
}
