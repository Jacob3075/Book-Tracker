package com.jacob.bookstore.services;

import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
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

	public void saveAndFlush(Book book) {
		bookRepository.saveAndFlush(book);
	}
}
