package com.blogapp.blog.services;

import java.util.List;

import com.blogapp.blog.payloads.CategoryDto;

public interface CategoryService {
	CategoryDto createCategory(CategoryDto category);
	
	CategoryDto updateCategory(CategoryDto category,Integer categoryId);
	
	CategoryDto getCategoryId(Integer categoryId);
	
	List<CategoryDto> getAllCategories();
	
	void deleteCategory(Integer categoryId);
}
