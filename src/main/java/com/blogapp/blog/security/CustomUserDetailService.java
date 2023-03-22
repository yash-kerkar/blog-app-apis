package com.blogapp.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.blogapp.blog.entities.User;
import com.blogapp.blog.exceptions.ResourceNotFoundException;
import com.blogapp.blog.repositories.UserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("user", "email: "+username, 0));
		return user;
	}

}
