package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
	Flux<Book> findBookByBookName(String name);
}
