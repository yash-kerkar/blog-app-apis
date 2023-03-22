package com.blogapp.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.blog.entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	Optional<User> findByEmail(String email);
}
