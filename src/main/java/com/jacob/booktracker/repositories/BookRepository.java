package com.jacob.booktracker.repositories;

import com.jacob.booktracker.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findBookByName(String name);
}
