package com.jacob.bookstore.repositories;

import com.jacob.bookstore.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
