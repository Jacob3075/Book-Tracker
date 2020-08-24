package com.jacob.booktracker.controllers;

import com.jacob.booktracker.config.MongoReactiveApplication;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.services.BookService;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = BookController.class)
@ExtendWith(SpringExtension.class)
@Import({BookService.class, MongoReactiveApplication.class, BookController.class})
class BookControllerTest {

	private final List<Book>  bookList    = new ArrayList<>();
	private final Set<String> authorSet   = new HashSet<>();
	private final Set<String> categorySet = new HashSet<>();
	private final String      baseUrl     = "http://localhost:8080/api.book-store/books/";

	@MockBean
	private BookRepository bookRepository;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		bookList.clear();
		authorSet.clear();
		categorySet.clear();

		Author author1 = new Author();
		author1.setId("1");
		author1.setAuthorName("Author 1");

		Author author2 = new Author();
		author2.setId("2");
		author2.setAuthorName("Author 2");

		authorSet.add(author1.getId());
		authorSet.add(author2.getId());

		Category category1 = new Category();
		Category category2 = new Category();

		category1.setId("1");
		category2.setId("2");

		category1.setCategoryName("Category 1");
		category2.setCategoryName("Category 2");

		categorySet.add(category1.getId());
		categorySet.add(category2.getId());

		Book book1 = new Book();
		book1.setId("1");
		book1.setBookName("Book 1");
		book1.setDescription("Description");
		book1.setPages(300);
		book1.setChapters(30);
		book1.setLastReadChapter(5);
		book1.setAuthorIds(authorSet);
		book1.setCategoryIds(categorySet);

		Book book2 = new Book();
		book2.setId("2");
		book2.setBookName("Book 2");
		book2.setDescription("Description");
		book2.setPages(400);
		book2.setChapters(35);
		book2.setLastReadChapter(15);
		book2.setAuthorIds(authorSet);
		book2.setCategoryIds(categorySet);

		bookList.add(book1);
		bookList.add(book2);
	}

	@Test
	void getAllBooksTest() {
		given(bookRepository.findAll()).willReturn(Flux.fromIterable(bookList));
		Book book = bookList.get(0);

		webTestClient.get()
		             .uri(baseUrl)
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$[0].id").isEqualTo(book.getId())
		             .jsonPath("$[0].bookName").isEqualTo(book.getBookName())
		             .jsonPath("$[0].description").isEqualTo(book.getDescription())
		             .jsonPath("$[0].pages").isEqualTo(book.getPages())
		             .jsonPath("$[0].chapters").isEqualTo(book.getChapters())
		             .jsonPath("$[0].lastReadChapter").isEqualTo(book.getLastReadChapter())
		             .jsonPath("$[0].authorIds").isArray()
		             .jsonPath("$[0].categoryIds").isArray();

		verify(bookRepository, times(1)).findAll();
	}

	@Test
	void getBookByIdTest() {
		Book book = bookList.get(0);
		given(bookRepository.findById(book.getId())).willReturn(Mono.just(book));

		webTestClient.get()
		             .uri(baseUrl + book.getId())
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(book.getId())
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription())
		             .jsonPath("$.authorIds").isArray()
		             .jsonPath("$.categoryIds").isArray();

		verify(bookRepository, times(1)).findById(book.getId());
	}

	@Test
	void addNewBookTest() {
		Book book = bookList.get(0);
		given(bookRepository.save(any())).willReturn(Mono.just(book));

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(book.getId())
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription());

		verify(bookRepository, times(1)).save(any());
	}

	@Test
	void updateBookTest() {
		Book book = bookList.get(0);
		given(bookRepository.save(any())).willReturn(Mono.just(book));

		webTestClient.put()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(book))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(book.getId())
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription());

		verify(bookRepository, times(1)).save(any());
	}

	@Test
	void deleteBookByIdTest() {
		Book book = bookList.get(0);
		given(bookRepository.deleteById(book.getId())).willReturn(Mono.empty());

		webTestClient.delete()
		             .uri(baseUrl + book.getId())
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody().isEmpty();

		verify(bookRepository, times(1)).deleteById(book.getId());
	}
}
