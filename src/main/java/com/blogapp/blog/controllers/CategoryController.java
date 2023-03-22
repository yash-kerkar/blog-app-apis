package com.blogapp.blog.controllers;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapp.blog.entities.Category;
import com.blogapp.blog.payloads.APIResponse;
import com.blogapp.blog.payloads.CategoryDto;
import com.blogapp.blog.services.impl.CategoryServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
	
	@Autowired
	private CategoryServiceImpl categoryService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		return new ResponseEntity<>(this.categoryService.createCategory(categoryDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId){
		return new ResponseEntity<>(this.categoryService.getCategoryId(categoryId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer categoryId,@Valid @RequestBody CategoryDto categoryDto){
		return new ResponseEntity<>(this.categoryService.updateCategory(categoryDto,categoryId),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
		this.categoryService.deleteCategory(categoryId);
		return ResponseEntity.ok(Map.of("message","Category deleted sucessfully"));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		List<CategoryDto> categories = this.categoryService.getAllCategories();
		return new ResponseEntity<>(categories,HttpStatus.OK);
	}
}
