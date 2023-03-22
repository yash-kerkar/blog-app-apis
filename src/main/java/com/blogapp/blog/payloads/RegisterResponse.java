package com.blogapp.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.blogapp.blog.entities.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterResponse {
	private int id;
	private String name;
	private String email;
	private String about;
	private Set<Role> roles = new HashSet<>();
}
