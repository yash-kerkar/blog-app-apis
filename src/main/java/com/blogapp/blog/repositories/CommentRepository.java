package com.blogapp.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogapp.blog.entities.Comment;
import com.blogapp.blog.entities.Post;
import com.blogapp.blog.entities.User;
import com.blogapp.blog.payloads.CommentDto;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	public List<Comment> findByPost(Post post);
	public List<Comment> findByUser(User user);
}
