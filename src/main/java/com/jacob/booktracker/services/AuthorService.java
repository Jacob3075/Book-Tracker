package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

	private final AuthorRepository authorRepository;

	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	public List<Author> findAll() {
		return authorRepository.findAll();
	}

	public boolean deleteById(Long id) {
		Optional<Author> optionalAuthor = this.findById(id);
		if (optionalAuthor.isPresent()) {
			authorRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	public Optional<Author> findById(Long id) {
		return authorRepository.findById(id);
	}

	public boolean updateAuthor(Long id, Author newAuthor) {
		return Author.stream(List.of(newAuthor))
		             .updateAuthor(id, authorRepository)
		             .saveAuthor(authorRepository);
	}

	public boolean addNewAuthor(Author author) {
		return Author.stream(List.of(author))
		             .ifNewAuthor(authorRepository)
		             .saveAuthor(authorRepository);

	}
}
