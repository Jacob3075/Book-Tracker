package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.ReactiveCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class CategoryService {
	private final ReactiveCategoryRepository reactiveCategoryRepository;

	public CategoryService(ReactiveCategoryRepository reactiveCategoryRepository) {
		this.reactiveCategoryRepository = reactiveCategoryRepository;
	}

	public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
		return ok().body(reactiveCategoryRepository.findAll(), Category.class);
	}

	public Mono<ServerResponse> findById(ServerRequest serverRequest) {
		return ok().body(reactiveCategoryRepository.findById(serverRequest.pathVariable("id")), Category.class);
	}

	public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
		return ok().body(reactiveCategoryRepository.deleteById(serverRequest.pathVariable("id")), Void.class);
	}

	public Mono<ServerResponse> addNewCategory(ServerRequest serverRequest) {
		return ok().body(
				serverRequest.bodyToMono(Category.class)
				             .flatMap(reactiveCategoryRepository::save),
				Category.class
		);
	}

	public Mono<ServerResponse> updateCategory(ServerRequest serverRequest) {
		return this.addNewCategory(serverRequest);
	}
}
