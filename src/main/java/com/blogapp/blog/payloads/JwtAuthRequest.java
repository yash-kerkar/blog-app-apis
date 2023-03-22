package com.blogapp.blog.payloads;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JwtAuthRequest {
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
}
