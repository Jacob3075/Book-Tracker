package com.jacob.booktracker.services;

import com.jacob.booktracker.dtos.response.BookDTO;
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

	public BookService(
			BookRepository bookRepository, AuthorRepository authorRepository,
			CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
	}

	public List<BookDTO> findAll() {
//		return bookRepository.findAll()
//		                     .stream()
//		                     .map(CommonUtils::convertToBookDTO)
//		                     .collect(Collectors.toList());
		return List.of();
	}

	public boolean deleteById(String id) {
//		Optional<Book> byId = bookRepository.findById(id);
//		if (byId.isPresent()) {
//			bookRepository.deleteById(id);
//			return true;
//		} else {
//			return false;
//		}
		return true;
	}

	public Optional<BookDTO> findById(String id) {
//		return CommonUtils.convertToBookDTO(bookRepository.findById(id));
		return Optional.empty();
	}

	public boolean updateBook(String id, Book newBook) {
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

	public boolean updateChapter(String id, int newChapter) {
//		Optional<Book> optionalBook = bookRepository.findById(id);
//		if (optionalBook.isEmpty()) return false;
//		Book book = optionalBook.get();
//		book.setLastReadChapter(newChapter);
//		bookRepository.save(book);
		return true;
	}
}
