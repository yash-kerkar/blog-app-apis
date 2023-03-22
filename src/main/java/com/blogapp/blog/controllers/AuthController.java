package com.blogapp.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.blog.exceptions.ApiException;
import com.blogapp.blog.payloads.JwtAuthRequest;
import com.blogapp.blog.payloads.JwtAuthResponse;
import com.blogapp.blog.payloads.RegisterRequest;
import com.blogapp.blog.payloads.RegisterResponse;
import com.blogapp.blog.security.JwtTokenHelper;
import com.blogapp.blog.services.impl.AuthServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private AuthServiceImpl authService;
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request)throws ApiException{
		try {
			RegisterResponse response = authService.register(request);
			return new ResponseEntity<>(response,HttpStatus.CREATED);
		}catch(DataIntegrityViolationException e) {
			throw new ApiException("Email already exists, Please login");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> creatToken(@Valid @RequestBody JwtAuthRequest request) throws Exception{
		
		return ResponseEntity.ok(authService.login(request));
	}
	

}
