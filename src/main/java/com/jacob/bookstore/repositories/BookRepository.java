package com.jacob.bookstore.repositories;

import com.jacob.bookstore.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findBookByName(String name);
}
