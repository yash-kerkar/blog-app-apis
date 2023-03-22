package com.blogapp.blog.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogapp.blog.entities.Comment;
import com.blogapp.blog.entities.Post;
import com.blogapp.blog.entities.Role;
import com.blogapp.blog.entities.User;
import com.blogapp.blog.exceptions.ApiException;
import com.blogapp.blog.exceptions.ResourceNotFoundException;
import com.blogapp.blog.payloads.JwtAuthRequest;
import com.blogapp.blog.payloads.JwtAuthResponse;
import com.blogapp.blog.payloads.RegisterRequest;
import com.blogapp.blog.payloads.RegisterResponse;
import com.blogapp.blog.payloads.UserDto;
import com.blogapp.blog.repositories.CommentRepository;
import com.blogapp.blog.repositories.PostRepository;
import com.blogapp.blog.repositories.RoleRepository;
import com.blogapp.blog.repositories.UserRepository;
import com.blogapp.blog.security.JwtTokenHelper;
import com.blogapp.blog.services.AuthService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	
	public RegisterResponse register(RegisterRequest request) throws DataIntegrityViolationException{
		User user = this.modelMapper.map(request, User.class);
		user.setPassword(this.passwordEncoder.encode(request.getPassword()));
		Role role = roleRepository.findByName("ROLE_USER");
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
			User savedUser = this.userRepository.save(user);
			RegisterResponse response = this.modelMapper.map(savedUser, RegisterResponse.class);
			return response;
	}
	
	public JwtAuthResponse login(JwtAuthRequest request) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
		}
		catch(BadCredentialsException e) {
			throw new ApiException("Invalid Username or Password");
		}
		User user = userRepository.findByEmail(request.getUsername()).orElseThrow(()->new ResourceNotFoundException("User", "username"+request.getUsername(), 0));
		String jwtToken = this.jwtTokenHelper.generateToken(user);
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(jwtToken);
		response.setUser(this.modelMapper.map(user, UserDto.class));
		return response;
	}
	
	public void isPostByUser(int postId,HttpServletRequest request) {
		String token = request.getHeader("Authorization").substring(7);
		String username = this.jwtTokenHelper.extractUsername(token);
		Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id",postId));
		User user = post.getUser();
		if(!(user.getEmail().equals(username))) {
			throw new ApiException("User is not authorised to perform this action");
		}
	}
	
	public void isCommentByUser(int commentId,HttpServletRequest request) {
		String token = request.getHeader("Authorization").substring(7);
		String username = this.jwtTokenHelper.extractUsername(token);
		Comment comment = this.commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "id",commentId));
		Post post = comment.getPost();
		User user = post.getUser();
		if(!(user.getEmail().equals(username))) {
			throw new ApiException("User is not authorised to perform this action");
		}
	}
}
