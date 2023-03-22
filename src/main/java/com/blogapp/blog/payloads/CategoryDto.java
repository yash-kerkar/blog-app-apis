package com.blogapp.blog.payloads;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
	private int id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
}
