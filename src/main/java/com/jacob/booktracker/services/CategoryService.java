package com.jacob.booktracker.services;

import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.CategoryRepository;
import com.jacob.booktracker.utils.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryDTO> findAll() {
		return categoryRepository.findAll()
		                         .stream()
		                         .map(CommonUtils::convertToCategoryDTO)
		                         .collect(Collectors.toList());
	}

	public boolean deleteById(Long id) {
		Optional<Category> byId = categoryRepository.findById(id);
		if (byId.isPresent()) {
			categoryRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	public Optional<CategoryDTO> findById(Long id) {
		return CommonUtils.convertToCategoryDTO(categoryRepository.findById(id));
	}

	public boolean addNewCategory(Category category) {
		categoryRepository.saveAndFlush(category);
		return true;
	}

	public boolean updateCategory(Long id, Category newCategory) {
		return Category.stream(List.of(newCategory))
		               .updateCategory(id, categoryRepository)
		               .saveCategory(categoryRepository);
	}
}
