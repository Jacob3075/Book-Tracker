package com.jacob.bookstore.repositories;

import com.jacob.bookstore.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	List<Author> findAuthorsByAuthorName(String name);

	Optional<Author> findAuthorByAuthorName(String name);
}
