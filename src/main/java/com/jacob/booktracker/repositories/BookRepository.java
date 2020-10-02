package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
	Mono<Book> findBookByBookName(String name);

	Mono<Boolean> existsByBookName(String name);
}
