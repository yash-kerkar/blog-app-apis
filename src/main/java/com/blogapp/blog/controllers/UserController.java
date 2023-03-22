package com.blogapp.blog.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.blog.payloads.UserDto;
import com.blogapp.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto createdUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<>(createdUserDto,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable Integer userId){
		UserDto createdUserDto = this.userService.updateUser(userDto,userId);
		return new ResponseEntity<>(createdUserDto,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(("/{userId}"))
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId){
		this.userService.deleteUser(userId);
		return ResponseEntity.ok(Map.of("message","User deleted sucessfully"));
	}
	
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
		UserDto user = this.userService.getUserById(userId);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		List<UserDto> users = this.userService.getAllUsers();
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
}
