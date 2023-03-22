package com.blogapp.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogapp.blog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	public Role findByName(String name);
}
