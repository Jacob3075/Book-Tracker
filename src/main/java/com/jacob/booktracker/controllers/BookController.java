package com.jacob.booktracker.controllers;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping(value = "/api.book-store/books/")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(value = "/")
	public List<Book> getBooks() {
		return bookService.findAll();
	}

	@GetMapping(value = "/{id}")
	public Optional<Book> getBookById(@PathVariable Long id) {
		return bookService.findById(id);
	}

	@PostMapping(value = "/")
	public String addNewBook(@RequestBody Book book) {
		if (bookService.addNewBook(book)) {
			return "CREATED";
		} else {
			throw new HttpClientErrorException(HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteBook(@PathVariable Long id, HttpServletResponse response) {
		if (bookService.deleteById(id)) {
			response.setStatus(SC_OK);
			return "DELETED";
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping(value = "/{id}")
	public String updateBook(@PathVariable Long id, @RequestBody Book book, HttpServletResponse response) {
		if (bookService.updateBook(id, book)) {
			response.setStatus(SC_OK);
			return "UPDATED";
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/{id}/last-read-chapter={newChapter}")
	public String updateLastReadChapter(@PathVariable Long id, @PathVariable int newChapter,
	                                    HttpServletResponse response) {
		if (bookService.updateChapter(id, newChapter)) {
			response.setStatus(SC_OK);
			return "CREATED";
		} else {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	}
}
