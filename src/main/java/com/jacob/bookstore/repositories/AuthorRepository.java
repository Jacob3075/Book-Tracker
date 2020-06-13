package com.jacob.bookstore.repositories;

import com.jacob.bookstore.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
