package com.jacob.bookstore.controllers;

import com.jacob.bookstore.models.Author;
import com.jacob.bookstore.services.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api.book-store/authors")
public class AuthorController {
	private final AuthorService authorService;

	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}

	@GetMapping(value = "/")
	public List<Author> getAuthors() {
		return authorService.findAll();
	}

	@GetMapping(value = "/{id}")
	public Author getAuthorById(@PathVariable Long id, HttpServletResponse response) {
		Optional<Author> optionalAuthor = authorService.findById(id);
		if (optionalAuthor.isPresent()) {
			response.setStatus(HttpServletResponse.SC_OK);
			return optionalAuthor.get();
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	@PostMapping(value = "/")
	public String addNewAuthor(@RequestBody Author author, HttpServletResponse response) {
		if (authorService.addNewAuthor(author)) {
			response.setStatus(HttpServletResponse.SC_CREATED);
			return "CREATED";
		} else {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "ALREADY EXISTS";
		}
	}

	@DeleteMapping(value = "/{id}")
	public String deleteAuthor(@PathVariable Long id, HttpServletResponse response) {
		if (authorService.deleteById(id)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "DELETED";
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}

	@PutMapping(value = "/{id}")
	public String updateAuthor(@PathVariable Long id, @RequestBody Author newAuthor, HttpServletResponse response) {
		if (authorService.updateAuthor(id, newAuthor)) {
			response.setStatus(HttpServletResponse.SC_OK);
			return "UPDATED";
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
		}
	}
}
