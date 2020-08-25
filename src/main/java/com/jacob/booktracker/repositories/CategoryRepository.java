package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
