package com.jacob.booktracker.services;

import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryDTO> findAll() {
//		return categoryRepository.findAll()
//		                         .stream()
//		                         .map(CommonUtils::convertToCategoryDTO)
//		                         .collect(Collectors.toList());
		return List.of();
	}

	public boolean deleteById(String id) {
//		Optional<Category> byId = categoryRepository.findById(id);
//		if (byId.isPresent()) {
//			categoryRepository.deleteById(id);
//			return true;
//		} else {
//			return false;
//		}
		return true;
	}

	public Optional<CategoryDTO> findById(String id) {
//		return CommonUtils.convertToCategoryDTO(categoryRepository.findById(id));
		return Optional.empty();
	}

	public boolean addNewCategory(Category category) {
		categoryRepository.save(category);
		return true;
	}

	public void updateCategory(String id, Category newCategory) {
//		return Category.stream(List.of(newCategory))
//		               .updateCategory(id, categoryRepository)
//		               .saveCategory(categoryRepository);
	}
}
