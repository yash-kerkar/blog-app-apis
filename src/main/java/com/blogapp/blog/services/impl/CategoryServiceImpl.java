package com.blogapp.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blogapp.blog.entities.Category;
import com.blogapp.blog.exceptions.ResourceNotFoundException;
import com.blogapp.blog.payloads.CategoryDto;
import com.blogapp.blog.repositories.CategoryRepository;
import com.blogapp.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category createdCategory = this.categoryRepository.save(category);
		return this.modelMapper.map(createdCategory,CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id",categoryId));
		category.setDescription(categoryDto.getDescription());
		category.setName(categoryDto.getName());
		Category updatedCategory = this.categoryRepository.save(category);
		return this.modelMapper.map(updatedCategory,CategoryDto.class);
	}

	@Override
	public CategoryDto getCategoryId(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id",categoryId));
		return this.modelMapper.map(category,CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = this.categoryRepository.findAll();
		List<CategoryDto> categoryDtos = categories.stream().map((cat)->this.modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
		return categoryDtos;
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "id",categoryId));
		this.categoryRepository.delete(category);
	}

}
