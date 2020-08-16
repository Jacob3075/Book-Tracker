package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AuthorService {

	private final AuthorRepository authorRepository;

	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	public Flux<Author> findAll() {
		return authorRepository.findAll();
	}

	public void deleteById(String id) {
		this.findById(id)
		    .subscribe(author -> authorRepository.deleteById(id));
//		if (optionalAuthor.isPresent()) {
//			authorRepository.deleteById(id);
//			return true;
//		} else {
//			return false;
//		}
//		return true;
	}

	public Mono<Author> findById(String id) {
		return authorRepository.findById(id);
	}

	public boolean updateAuthor(String id, Author newAuthor) {
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
