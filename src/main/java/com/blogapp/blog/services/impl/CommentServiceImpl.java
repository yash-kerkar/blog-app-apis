package com.blogapp.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.blog.entities.Comment;
import com.blogapp.blog.entities.Post;
import com.blogapp.blog.entities.User;
import com.blogapp.blog.exceptions.ResourceNotFoundException;
import com.blogapp.blog.payloads.CommentDto;
import com.blogapp.blog.payloads.PostDto;
import com.blogapp.blog.repositories.CommentRepository;
import com.blogapp.blog.repositories.PostRepository;
import com.blogapp.blog.repositories.UserRepository;
import com.blogapp.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public CommentDto createComment(CommentDto commentDto,Integer postId,Integer userId) {
		Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));
		User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
		Comment createdComment = this.commentRepository.save(comment);
		return this.modelMapper.map(createdComment, CommentDto.class);
	}

	@Override
	public CommentDto getCommentById(Integer id) {
		Comment comment = this.commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment", "id", id));
		return this.modelMapper.map(comment, CommentDto.class);
	}


	@Override
	public List<CommentDto> getCommentByPost(Integer id) {
		Post post = this.postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
		List<Comment> comments = this.commentRepository.findByPost(post);
		List<CommentDto> commentsDto = comments.stream().map((comment)->this.modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
		return commentsDto;
	}

	@Override
	public List<CommentDto> getCommentByUser(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteComment(Integer id) {
		Comment comment = this.commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment", "id", id));
		this.commentRepository.delete(comment);
	}

}
