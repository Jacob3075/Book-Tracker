package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

	Flux<Category> findCategoriesByCategoryName(String name);


	Mono<Category> findCategoryByCategoryName(String name);
}
