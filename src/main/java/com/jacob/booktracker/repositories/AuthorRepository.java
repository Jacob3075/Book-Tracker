package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

	Flux<Author> findAuthorsByAuthorName(String name);

	Mono<Author> findAuthorByAuthorName(String name);
}
