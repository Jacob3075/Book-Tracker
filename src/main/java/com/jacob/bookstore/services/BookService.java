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
public class BookService {

//	TODO: FIX SO THAT DUPLICATE BOOKS ARE NOT ADDED

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
		Optional<Book> optionalBook = this.findById(id);
		if (optionalBook.isPresent()) {
			Book oldBook = optionalBook.get();
			BeanUtils.copyProperties(newBook, oldBook, "id");
			bookRepository.saveAndFlush(oldBook);
			return true;
		}
		return false;
	}

	public boolean addNewBook(Book book) {
//		if (this.isNewBook(book)) {
		addAuthorsFromBook(book.getAuthors());
		bookRepository.saveAndFlush(book);
		return true;
//		} else {
//			return false;
//		}
	}

//	private boolean isNewBook(Book book) {
//		return true;
//		return this.findById(book.getId()).isEmpty();
//	}

	public void addAuthorsFromBook(List<Author> authors) {
		List<Author> newAuthors = authors.stream()
		                                 .filter(this::isNewAuthor)
		                                 .collect(Collectors.toList());
		newAuthors.forEach(authorRepository::saveAndFlush);
//		authorRepository.flush();
	}

	private boolean isNewAuthor(Author author) {
//		boolean isNew = this.findById(author.getId()).isEmpty();
//		if (isNew) {
//			author.setId(null);
//		}
		return true;
	}

}
