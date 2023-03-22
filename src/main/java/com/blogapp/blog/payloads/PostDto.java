package com.blogapp.blog.payloads;


import java.util.Date;

import com.blogapp.blog.entities.Category;
import com.blogapp.blog.entities.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private int id;
    
	@NotEmpty
	private String title;
	
	@NotEmpty
	private String content;
	
	private String imageName;
	
	private Date date;
	
	private UserDto user;
	
	private CategoryDto category;
	
}
