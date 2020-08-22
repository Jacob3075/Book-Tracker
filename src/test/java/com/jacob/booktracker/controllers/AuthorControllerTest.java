package com.jacob.booktracker.controllers;

import com.jacob.booktracker.config.MongoReactiveApplication;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = AuthorController.class)
@ExtendWith(SpringExtension.class)
@Import({AuthorService.class, MongoReactiveApplication.class, AuthorController.class})
class AuthorControllerTest {

	private final String           baseUri = "http://localhost:8080/api.book-store/authors/";
	@MockBean
	private       AuthorRepository authorRepository;

	@Autowired
	private WebTestClient webTestClient;
	private Author        author1;
	private Author        author2;


	@BeforeEach
	void setUp() {
		author1 = new Author();
		author1.setAuthorName("Name 1");
		author1.setId("1");
		author1.setBooks(List.of(new Book()));

		author2 = new Author();
		author2.setId("2");
		author2.setAuthorName("Name 2");
		author2.setBooks(List.of(new Book()));
	}

	@Test
	void getAllAuthorsTest() {
		given(authorRepository.findAll()).willReturn(Flux.fromIterable(List.of(author1, author2)));

		webTestClient.get()
		             .uri(baseUri)
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$[0].id").isEqualTo(author1.getId())
		             .jsonPath("$[0].authorName").isEqualTo(author1.getAuthorName())
		             .jsonPath("$[1].id").isEqualTo(author2.getId())
		             .jsonPath("$[1].authorName").isEqualTo(author2.getAuthorName());

	}

	@Test
	void getAuthorByIdTest() {
		given(authorRepository.findById(author1.getId())).willReturn(Mono.just(author1));

		webTestClient.get()
		             .uri(baseUri + author1.getId())
		             .exchange()
		             .expectStatus()
		             .isOk()
		             .expectBody()
		             .jsonPath("$.authorName").isEqualTo(author1.getAuthorName());
	}

	@Test
	void addNewAuthorTest() {
		given(authorRepository.save(any())).willReturn(Mono.just(author1));

		webTestClient.post()
		             .uri(baseUri)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(author1))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.authorName").isEqualTo(author1.getAuthorName())
		             .jsonPath("$.id").isEqualTo(author1.getId());

		verify(authorRepository, times(1)).save(any());
	}

	@Test
	void updateAuthorTest() {
		given(authorRepository.save(any())).willReturn(Mono.just(author1));

		webTestClient.put()
		             .uri(baseUri)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(author1))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(author1.getId())
		             .jsonPath("$.authorName").isEqualTo(author1.getAuthorName());

		verify(authorRepository, times(1)).save(any());
	}

	@Test
	void deleteAuthorByIdTest() {
		given(authorRepository.deleteById(author1.getId())).willReturn(Mono.empty());

		webTestClient.delete()
		             .uri(baseUri + author1.getId())
		             .exchange()
		             .expectStatus().isOk();

		verify(authorRepository, times(1)).deleteById(author1.getId());
	}
}
