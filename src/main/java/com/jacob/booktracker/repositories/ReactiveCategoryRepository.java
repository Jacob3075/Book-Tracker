package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReactiveCategoryRepository extends ReactiveMongoRepository<Category, String> {

	List<Category> findCategoriesByCategoryName(String name);


	Optional<Category> findCategoryByCategoryName(String name);
}
