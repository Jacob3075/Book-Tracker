package com.jacob.booktracker.controllers;

import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.services.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@RestController
//@RequestMapping(value = "/api.book-store/categories/")
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
	public CategoryDTO findById(@PathVariable String id) {
		Optional<CategoryDTO> optionalCategory = categoryService.findById(id);
//		if (optionalCategory.isPresent()) {
//			response.setStatus(SC_OK);
//			return optionalCategory.get();
//		} else {
//			response.setStatus(SC_NOT_FOUND);
//			return null;
//		}
		return null;
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
	public String deleteCategory(@PathVariable String id) {
//		if (categoryService.deleteById(id)) {
//			response.setStatus(HttpServletResponse.SC_OK);
//			return "DELETED";
//		} else {
//			response.setStatus(SC_NOT_FOUND);
//			return "NOT FOUND";
//		}
		return null;
	}

	@PutMapping(value = "/{id}")
	public String updateCategory(
			@PathVariable String id, @RequestBody Category newCategory) {
//		if (categoryService.updateCategory(id, newCategory)) {
//			response.setStatus(HttpServletResponse.SC_OK);
//			return "UPDATED";
//		} else {
//			response.setStatus(SC_NOT_FOUND);
//			return "NOT FOUND";
//		}
		return null;
	}

}
