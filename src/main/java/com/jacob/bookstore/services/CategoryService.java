package com.jacob.bookstore.services;

import com.jacob.bookstore.models.Category;
import com.jacob.bookstore.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public boolean deleteById(Long id) {
		Optional<Category> optionalCategory = this.findById(id);
		if (optionalCategory.isPresent()) {
			categoryRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	public Optional<Category> findById(Long id) {
		return categoryRepository.findById(id);
	}

	public void saveAndFlush(Category category) {
		categoryRepository.saveAndFlush(category);
	}
}
