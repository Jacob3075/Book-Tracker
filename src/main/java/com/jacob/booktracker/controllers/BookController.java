package com.jacob.booktracker.controllers;

import com.jacob.booktracker.dtos.response.BookDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.services.BookService;
import org.springframework.web.bind.annotation.*;

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
	public List<BookDTO> getBooks() {
		return bookService.findAll();
	}

	@GetMapping(value = "/{id}")
	public BookDTO getBookById(@PathVariable String id) {
		Optional<BookDTO> optionalBookDTO = bookService.findById(id);
//		if (optionalBookDTO.isPresent()) {
//			response.setStatus(SC_OK);
//			return optionalBookDTO.get();
//		} else {
//			response.setStatus(SC_NOT_FOUND);
			return null;
//		}
	}

	@PostMapping(value = "/")
	public String addNewBook(@RequestBody Book book) {
//		if (bookService.addNewBook(book)) {
//			response.setStatus(SC_OK);
//			return "CREATED";
//		} else {
//			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
//		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteBook(@PathVariable String id) {
//		if (bookService.deleteById(id)) {
//			response.setStatus(SC_OK);
//			return "DELETED";
//		} else {
//			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
//		}

	}

	@PutMapping(value = "/{id}")
	public String updateBook(@PathVariable String id, @RequestBody Book book) {
//		if (bookService.updateBook(id, book)) {
//			response.setStatus(SC_OK);
//			return "UPDATED";
//		} else {
//			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
//		}
	}

	@PostMapping(value = "/{id}/last-read-chapter={newChapter}")
	public String updateLastReadChapter(
			@PathVariable String id, @PathVariable int newChapter) {
//		if (bookService.updateChapter(id, newChapter)) {
//			response.setStatus(SC_OK);
//			return "UPDATED";
//		} else {
//			response.setStatus(SC_NOT_FOUND);
			return "NOT FOUND";
//		}
	}
}
