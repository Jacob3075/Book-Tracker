package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
	List<Book> findBookByName(String name);
}
