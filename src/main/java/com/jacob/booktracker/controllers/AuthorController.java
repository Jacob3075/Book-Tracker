package com.jacob.booktracker.controllers;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.services.AuthorService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/api.book-store/authors")
public class AuthorController {
	private final AuthorService authorService;

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	@GetMapping(value = "/")
	public Flux<Author> getAuthors() {
		return authorService.findAll();
	}

	@GetMapping(value = "/{id}")
	public Author getAuthorById(@PathVariable String id) {
//		Optional<Author> optionalAuthor = authorService.findById(id);
//		if (optionalAuthor.isPresent()) {
//			response.setStatus(HttpServletResponse.SC_OK);
//			return optionalAuthor.get();
//		} else {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			return null;
//		}
		return null;
	}

	@PostMapping(value = "/")
	public String addNewAuthor(@RequestBody Author author) {
//		if (authorService.addNewAuthor(author)) {
//			response.setStatus(HttpServletResponse.SC_CREATED);
//			return "CREATED";
//		} else {
//			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "ALREADY EXISTS";
//		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteAuthor(@PathVariable String id) {
//		if (authorService.deleteById(id)) {
//			response.setStatus(HttpServletResponse.SC_OK);
//			return "DELETED";
//		} else {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
//		}
	}

	@PutMapping(value = "/{id}")
	public String updateAuthor(@PathVariable String id, @RequestBody Author newAuthor) {
//		if (authorService.updateAuthor(id, newAuthor)) {
//			response.setStatus(HttpServletResponse.SC_OK);
//			return "UPDATED";
//		} else {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
//		}
	}
}
