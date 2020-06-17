package com.jacob.booktracker.controllers;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.services.BookService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

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
			return "ALREADY EXISTS";
		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteBook(@PathVariable Long id, HttpServletResponse response) {
		if (bookService.deleteById(id)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "DELETED";
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
		}

	}

	@PutMapping(value = "/{id}")
	public String updateBook(@PathVariable Long id, @RequestBody Book book, HttpServletResponse response) {
		if (bookService.updateBook(id, book)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "UPDATED";
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}
}
