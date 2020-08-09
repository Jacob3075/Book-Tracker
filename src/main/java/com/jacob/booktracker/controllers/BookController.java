package com.jacob.booktracker.controllers;

import com.jacob.booktracker.dtos.response.BookDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.services.BookService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping(value = "/api.book-store/books/")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(value = "/")
	public List<BookDTO> getBooks() {
		return bookService.findAll();
	}

	@GetMapping(value = "/{id}")
	public BookDTO getBookById(@PathVariable Long id, HttpServletResponse response) {
		Optional<BookDTO> optionalBookDTO = bookService.findById(id);
		if (optionalBookDTO.isPresent()) {
			response.setStatus(SC_OK);
			return optionalBookDTO.get();
		} else {
			response.setStatus(SC_NOT_FOUND);
			return null;
		}
	}

	@PostMapping(value = "/")
	public String addNewBook(@RequestBody Book book, HttpServletResponse response) {
		if (bookService.addNewBook(book)) {
			response.setStatus(SC_OK);
			return "CREATED";
		} else {
			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteBook(@PathVariable Long id, HttpServletResponse response) {
		if (bookService.deleteById(id)) {
			response.setStatus(SC_OK);
			return "DELETED";
		} else {
			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
		}

	}

	@PutMapping(value = "/{id}")
	public String updateBook(@PathVariable Long id, @RequestBody Book book, HttpServletResponse response) {
		if (bookService.updateBook(id, book)) {
			response.setStatus(SC_OK);
			return "UPDATED";
		} else {
			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}

	@PostMapping(value = "/{id}/last-read-chapter={newChapter}")
	public String updateLastReadChapter(
			@PathVariable Long id, @PathVariable int newChapter,
			HttpServletResponse response) {
		if (bookService.updateChapter(id, newChapter)) {
			response.setStatus(SC_OK);
			return "UPDATED";
		} else {
			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}
}
