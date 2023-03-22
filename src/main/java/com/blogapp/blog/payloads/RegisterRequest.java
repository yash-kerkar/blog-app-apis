package com.blogapp.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
	@Size(min=4,message="User name must be min 4 characters")
	private String name;
	@NotEmpty
	@Email(message="Email address not valid")
	private String email;
	@NotEmpty
	@Size(min=3,max=10,message="Password must be min 3 chars and max 10 chars")
	private String password;
	@NotEmpty
	private String about;
}
