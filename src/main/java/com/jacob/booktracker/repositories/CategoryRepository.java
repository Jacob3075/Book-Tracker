package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findCategoriesByCategoryName(String name);


	Optional<Category> findCategoryByCategoryName(String name);
}
