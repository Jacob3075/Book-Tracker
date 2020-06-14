package com.jacob.bookstore.controllers;

import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.services.BookService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api.book-store/")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(value = "/books/")
	public List<Book> getBooks() {
		return bookService.findAll();
	}

	@GetMapping(value = "books/{id}/")
	public Optional<Book> getBookById(@PathVariable Long id) {
		return bookService.findById(id);
	}

	@PostMapping(value = "/books")
	public String addNewBook(@RequestBody Book book) {
		System.out.println("book = " + book);
		if (bookService.addNewBook(book)) {
			return "Created";
		} else {
			return "Already exists";
		}
	}

	@DeleteMapping(value = "/books/{id}")
	public void deleteBook(@PathVariable Long id, HttpServletResponse response) {
		if (bookService.deleteById(id)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	@PutMapping(value = "books/{id}")
	public void updateBook(@PathVariable Long id, @RequestBody Book book, HttpServletResponse response) {
		if (bookService.updateBook(id, book)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
