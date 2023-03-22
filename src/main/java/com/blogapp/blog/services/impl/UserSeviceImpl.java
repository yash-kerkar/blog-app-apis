package com.blogapp.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.blog.entities.User;
import com.blogapp.blog.exceptions.ResourceNotFoundException;
import com.blogapp.blog.payloads.UserDto;
import com.blogapp.blog.repositories.UserRepository;
import com.blogapp.blog.services.UserService;

@Service
public class UserSeviceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto createUser(UserDto userDto) {
		
		User user = this.dtoToUser(userDto);
		
		User savedUser = userRepository.save(user);
		
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(userDto.getPassword());
		
		User updatedUser = userRepository.save(user);
		return this.userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepository.findAll();
		List<UserDto> usersDto = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		return usersDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		
		this.userRepository.delete(user);
		
	}
	
	private User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}
	
	private UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

}
