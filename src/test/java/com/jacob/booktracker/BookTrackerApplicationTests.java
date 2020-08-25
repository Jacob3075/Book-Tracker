package com.jacob.booktracker;

import com.jacob.booktracker.config.MongoReactiveApplication;
import com.jacob.booktracker.controllers.BookController;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@WebFluxTest(controllers = BookController.class)
@ExtendWith(SpringExtension.class)
@Import({MongoReactiveApplication.class, BookController.class, BookService.class})
class BookTrackerApplicationTests {

	private final String        baseUrl = "http://localhost:8080/api.book-store/books/";
	@Autowired
	private       WebTestClient webTestClient;

	@Test
	void integrationTest_getAllBooks() {
		webTestClient.get()
		             .uri(baseUrl)
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .jsonPath("$").isArray();
	}

	@Test
	void integrationTest_getBookById_present() {
		webTestClient.get()
		             .uri(baseUrl + "1")
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .jsonPath("$").isNotEmpty();
	}

	@Test
//	TODO
	void integrationTest_getBookById_notPresent() {
		webTestClient.get()
		             .uri(baseUrl + "2")
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}

	@Test
	void integrationTest_addNewBook_isOk() {
		Book book = new Book();
		book.setId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription())
		             .jsonPath("$.chapters").isEqualTo(book.getChapters())
		             .jsonPath("$.lastReadChapter").isEqualTo(book.getLastReadChapter())
		             .jsonPath("$.pages").isEqualTo(book.getPages());
	}

	@Test
//	TODO
	void integrationTest_addNewBook_error_alreadyExists() {
		Book book = new Book();
//		book.setId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}

	@Test
//	TODO
	void integrationTest_addNewBook_error_invalidAuthorId() {
		Book book = new Book();
//		book.setId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}

	@Test
//	TODO
	void integrationTest_addNewBook_error_invalidCategoryId() {
		Book book = new Book();
//		book.setId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}

	@Test
	void integrationTest_updateBook_present() {
		Book book = new Book();
		book.setId("");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription())
		             .jsonPath("$.chapters").isEqualTo(book.getChapters())
		             .jsonPath("$.lastReadChapter").isEqualTo(book.getLastReadChapter())
		             .jsonPath("$.pages").isEqualTo(book.getPages());
	}

	@Test
//	TODO
	void integrationTest_updateBook_notPresent() {
		Book book = new Book();
		book.setId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		webTestClient.put()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}

	@Test
	void integrationTest_deleteBookById_present() {
		webTestClient.delete()
		             .uri(baseUrl + "5f37ff8e6a7d681076c1486e")
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}

	@Test
//	TODO
	void integrationTest_deleteBookById_notPresent() {
		webTestClient.delete()
		             .uri(baseUrl + "2")
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}
}
