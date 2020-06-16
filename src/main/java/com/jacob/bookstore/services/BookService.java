package com.jacob.bookstore.services;

import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.repositories.AuthorRepository;
import com.jacob.bookstore.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository   bookRepository;
	private final AuthorRepository authorRepository;

	public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	public boolean deleteById(Long id) {
		Optional<Book> optionalBook = this.findById(id);
		if (optionalBook.isPresent()) {
			bookRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Optional<Book> findById(Long id) {
		return bookRepository.findById(id);
	}

	public boolean updateBook(Long id, Book newBook) {
		return Book.stream(List.of(newBook))
		           .ifNotNewBook(bookRepository)
		           .addNewAuthorsFromBook(authorRepository)
		           .updateBook(
				           id,
				           newBook,
				           bookRepository,
				           authorRepository
		           )
		           .saveBook(bookRepository);
	}

	public boolean addNewBook(Book book) {
		return Book.stream(List.of(book))
		           .ifNewBook(bookRepository)
		           .addNewAuthorsFromBook(authorRepository)
		           .updateAuthors(authorRepository)
		           .saveBook(bookRepository);
	}
}
