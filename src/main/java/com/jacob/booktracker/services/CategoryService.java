package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

//
//	public List<CategoryDTO> findAll() {
////		return categoryRepository.findAll()
////		                         .stream()
////		                         .map(CommonUtils::convertToCategoryDTO)
////		                         .collect(Collectors.toList());
//		return List.of();
//	}
//
//	public boolean deleteById(String id) {
////		Optional<Category> byId = categoryRepository.findById(id);
////		if (byId.isPresent()) {
////			categoryRepository.deleteById(id);
////			return true;
////		} else {
////			return false;
////		}
//		return true;
//	}
//
//	public Optional<CategoryDTO> findById(String id) {
////		return CommonUtils.convertToCategoryDTO(categoryRepository.findById(id));
//		return Optional.empty();
//	}
//
//	public boolean addNewCategory(Category category) {
//		categoryRepository.save(category);
//		return true;
//	}
//
//	public void updateCategory(String id, Category newCategory) {
////		return Category.stream(List.of(newCategory))
////		               .updateCategory(id, categoryRepository)
////		               .saveCategory(categoryRepository);
//	}

	public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
		return ok().body(categoryRepository.findAll(), Category.class);
	}

	public Mono<ServerResponse> findById(ServerRequest serverRequest) {
		return ok().body(categoryRepository.findById(serverRequest.pathVariable("id")), Category.class);
	}

	public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
		return ok().body(categoryRepository.deleteById(serverRequest.pathVariable("id")), Void.class);
	}

	public Mono<ServerResponse> addNewCategory(ServerRequest serverRequest) {
		return ok().body(
				serverRequest.bodyToMono(Category.class)
				             .flatMap(categoryRepository::save),
				Category.class
		);
	}

	public Mono<ServerResponse> updateCategory(ServerRequest serverRequest) {
		return this.addNewCategory(serverRequest);
	}
}
