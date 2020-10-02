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
