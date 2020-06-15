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
	public void addNewAuthor(@RequestBody Author author, HttpServletResponse response) {
		if (authorService.addNewAuthor(author)) {
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@DeleteMapping(value = "/{id}")
	public void deleteAuthor(@PathVariable Long id, HttpServletResponse response) {
		if (authorService.deleteById(id)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@PutMapping(value = "/{id}")
	public void updateAuthor(@PathVariable Long id, @RequestBody Author newAuthor, HttpServletResponse response) {
		if (authorService.updateAuthor(id, newAuthor)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
