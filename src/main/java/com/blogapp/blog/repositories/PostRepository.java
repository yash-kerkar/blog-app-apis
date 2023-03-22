package com.blogapp.blog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.blog.entities.Category;
import com.blogapp.blog.entities.Post;
import com.blogapp.blog.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer>{
	Page<Post> findByUser(User user,Pageable p);
	Page<Post> findByCategory(Category category,Pageable p);
	Page<Post> findByTitleContaining(String title,Pageable p);
}
