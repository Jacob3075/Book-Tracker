package com.jacob.bookstore.services;

import com.jacob.bookstore.models.Author;
import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.repositories.AuthorRepository;
import com.jacob.bookstore.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
		Optional<Book> optionalBook = this.findById(id);
		if (optionalBook.isPresent()) {
			Book oldBook = optionalBook.get();

			BeanUtils.copyProperties(newBook, oldBook, "id", "authors");

			newBook.getAuthors()
			       .stream()
			       .filter(this::isNewAuthor)
			       .forEach(authorRepository::saveAndFlush);

			List<String> oldAuthorNames =
					oldBook.getAuthors()
					       .stream()
					       .map(Author::getAuthorName)
					       .collect(Collectors.toList());

			List<String> newAuthorNames = newBook.getAuthors()
			                                     .stream()
			                                     .map(Author::getAuthorName)
			                                     .collect(Collectors.toList());

			oldBook.getAuthors()
			       .removeIf(author -> !newAuthorNames.contains(author.getAuthorName()));

			newBook.getAuthors()
			       .stream()
			       .filter(author -> !oldAuthorNames.contains(author.getAuthorName()))
			       .map(Author::getAuthorName)
			       .forEach(authorName -> oldBook.getAuthors()
			                                     .add(authorRepository.findAuthorByAuthorName(authorName).get()));

			bookRepository.saveAndFlush(oldBook);

			return true;
		} else {
			return false;
		}
	}

	private boolean isNewAuthor(Author author) {
		return authorRepository.findAuthorsByAuthorName(author.getAuthorName()).isEmpty();
	}

	public boolean addNewBook(Book book) {
		if (this.isNewBook(book)) {
			addAuthorsFromBook(book.getAuthors());
			Book newBook = new Book();
			newBook.setName(book.getName());
			newBook.setDescription(book.getDescription());

			book.getAuthors()
			    .stream()
			    .filter(Predicate.not(this::isNewAuthor))
			    .map(Author::getAuthorName)
			    .map(authorRepository::findAuthorByAuthorName)
			    .filter(Optional::isPresent)
			    .map(author -> author.get().getId())
			    .forEach(id -> newBook.getAuthors().add(authorRepository.findById(id).get()));


			bookRepository.saveAndFlush(newBook);
			return true;
		} else {
			return false;
		}
	}

	private boolean isNewBook(Book book) {
		return bookRepository.findBookByName(book.getName()).isEmpty();
	}

	public void addAuthorsFromBook(List<Author> authors) {
		List<Author> newAuthors = authors.stream()
		                                 .filter(this::isNewAuthor)
		                                 .collect(Collectors.toList());
		newAuthors.forEach(authorRepository::saveAndFlush);
	}
}
