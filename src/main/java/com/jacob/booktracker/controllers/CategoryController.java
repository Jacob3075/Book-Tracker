package com.jacob.booktracker.controllers;

import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.services.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping(value = "/api.book-store/categories/")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping(value = "/")
	public List<CategoryDTO> getAllCategories() {
		return categoryService.findAll();
	}

	@GetMapping(value = "/{id}")
	public CategoryDTO findById(@PathVariable Long id, HttpServletResponse response) {
		Optional<CategoryDTO> optionalCategory = categoryService.findById(id);
		if (optionalCategory.isPresent()) {
			response.setStatus(SC_OK);
			return optionalCategory.get();
		} else {
			response.setStatus(SC_NOT_FOUND);
			return null;
		}
	}

	@PostMapping(value = "/")
	public String addNewCategory(@RequestBody Category category) {
		if (categoryService.addNewCategory(category)) {
			return "CREATED";
		} else {
			return "NOT FOUND";
		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteCategory(@PathVariable Long id, HttpServletResponse response) {
		if (categoryService.deleteById(id)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "DELETED";
		} else {
			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}

	@PutMapping(value = "/{id}")
	public String updateCategory(
			@PathVariable Long id, @RequestBody Category newCategory,
			HttpServletResponse response) {
		if (categoryService.updateCategory(id, newCategory)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "UPDATED";
		} else {
			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}

}
