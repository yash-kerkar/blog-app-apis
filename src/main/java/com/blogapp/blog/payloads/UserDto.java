package com.blogapp.blog.payloads;

import java.util.ArrayList;

import com.blogapp.blog.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserDto {
	private int id;
	@NotEmpty
	@Size(min=4,message="User name must be min 4 characters")
	private String name;
	@Email(message="Email address not valid")
	private String email;
	@NotEmpty
	@Size(min=3,max=10,message="Password must be min 3 chars and max 10 chars")
	private String password;
	@NotEmpty
	private String about;
	
	private ArrayList<Role> roles;
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
}
