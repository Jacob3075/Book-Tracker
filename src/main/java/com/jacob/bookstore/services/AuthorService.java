package com.jacob.bookstore.services;

import com.jacob.bookstore.models.Author;
import com.jacob.bookstore.repositories.AuthorRepository;
import org.springframework.beans.BeanUtils;
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
		Optional<Author> optionalAuthor = this.findById(id);
		if (optionalAuthor.isPresent()) {
			System.out.println("newAuthor = " + newAuthor);
			Author oldAuthor = optionalAuthor.get();
			BeanUtils.copyProperties(newAuthor, oldAuthor, "id");
			authorRepository.saveAndFlush(oldAuthor);
			return true;
		} else {
			return false;
		}
	}

	public boolean addNewAuthor(Author author) {
		if (this.isNewAuthor(author)) {
			authorRepository.saveAndFlush(author);
			return true;
		} else {
			return false;
		}
	}

	private boolean isNewAuthor(Author author) {
		return authorRepository.findAuthorsByAuthorName(author.getAuthorName()).isEmpty();
	}
}
