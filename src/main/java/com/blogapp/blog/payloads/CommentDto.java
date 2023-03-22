package com.blogapp.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
	private int id;
	@NotBlank
	private String content;
	private UserDto user;
	private PostDto post;
}
