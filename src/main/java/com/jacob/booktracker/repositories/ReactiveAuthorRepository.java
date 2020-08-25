package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactiveAuthorRepository extends ReactiveMongoRepository<Author, String> {

	List<Author> findAuthorsByAuthorName(String name);

	Optional<Author> findAuthorByAuthorName(String name);
}
