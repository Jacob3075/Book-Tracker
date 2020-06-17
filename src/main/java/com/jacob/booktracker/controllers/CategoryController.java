package com.jacob.booktracker.controllers;

import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController(value = "/api.book-store/categories/")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping(value = "/")
	public List<Category> getAllCategories() {
		return categoryService.findAll();
	}

	@GetMapping(value = "/{id}")
	public Category findById(@PathVariable Long id) {
		Optional<Category> optionalCategory = categoryService.findById(id);
		if (optionalCategory.isPresent()) {
			return optionalCategory.get();
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/")
	public String addNewCategory(@RequestBody Category category, HttpServletResponse response) {
		if (categoryService.addNewCategory(category)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "CREATED";
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteCategory(@PathVariable Long id, HttpServletResponse response) {
		if (categoryService.deleteById(id)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "DELETED";
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{id}")
	public String updateCategory(@PathVariable Long id, @RequestBody Category newCategory,
	                             HttpServletResponse response) {
		if (categoryService.updateCategory(id, newCategory)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "UPDATED";
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}

}
