package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
