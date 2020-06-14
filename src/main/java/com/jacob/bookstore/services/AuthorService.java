package com.jacob.bookstore.services;

import com.jacob.bookstore.models.Author;
import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.repositories.AuthorRepository;
import com.jacob.bookstore.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

	// TODO: FIX SO THAT DUPLICATE AUTHORS ARE NOT ADDED

	private final AuthorRepository authorRepository;
	private final BookRepository   bookRepository;

	public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
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
			Author oldAuthor = optionalAuthor.get();
			BeanUtils.copyProperties(newAuthor, oldAuthor, "id");
			authorRepository.saveAndFlush(oldAuthor);
			return true;
		} else {
			return false;
		}
	}

	public boolean addNewAuthor(Author author) {
		addBooksFromAuthor(author.getBooks());
		if (this.isNewAuthor(author)) {
			authorRepository.saveAndFlush(author);
			return true;
		} else {
			return false;
		}
	}

	// TODO: FIX
	public void addBooksFromAuthor(List<Book> books) {
		List<Book> newBooks = books.stream()
		                           .filter(this::isNewBook)
		                           .collect(Collectors.toList());
		bookRepository.saveAll(newBooks);
//		bookRepository.flush();
	}

	private boolean isNewAuthor(Author author) {
		return true;
//		return this.findById(author.getId()).isEmpty();
	}

	//
	private boolean isNewBook(Book book) {
		return true;
//		return this.findById(book.getId()).isEmpty();
	}

}
