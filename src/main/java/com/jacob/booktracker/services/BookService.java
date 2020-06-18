package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository     bookRepository;
	private final AuthorRepository   authorRepository;
	private final CategoryRepository categoryRepository;

	public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
	                   CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
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
		           .addNewCategoriesFromBook(categoryRepository)
		           .updateBook(
				           id,
				           bookRepository,
				           authorRepository,
				           categoryRepository
		           )
		           .saveBook(bookRepository);
	}

	public boolean addNewBook(Book book) {
		return Book.stream(List.of(book))
		           .ifNewBook(bookRepository)
		           .addNewAuthorsFromBook(authorRepository)
		           .addNewCategoriesFromBook(categoryRepository)
		           .updateAuthors(authorRepository)
		           .updateCategories(categoryRepository)
		           .saveBook(bookRepository);
	}

	public boolean updateChapter(Long id, int newChapter) {
		Optional<Book> optionalBook = bookRepository.findById(id);
		if (optionalBook.isEmpty()) return false;
		Book book = optionalBook.get();
		book.setLastReadChapter(newChapter);
		bookRepository.saveAndFlush(book);
		return true;
	}
}
