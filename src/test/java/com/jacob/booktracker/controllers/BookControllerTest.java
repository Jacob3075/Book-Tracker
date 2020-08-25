package com.jacob.booktracker.controllers;

import com.jacob.booktracker.config.MongoReactiveApplication;
import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.services.BookService;
import com.jacob.booktracker.utils.CommonUtils;
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
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = BookController.class)
@ExtendWith(SpringExtension.class)
@Import({BookService.class, MongoReactiveApplication.class, BookController.class})
class BookControllerTest {

	private final List<Book>    bookList    = new ArrayList<>();
	private final Set<String>   authorSet   = new HashSet<>();
	private final Set<String>   categorySet = new HashSet<>();
	private final List<BookDTO> bookDTOs    = new ArrayList<>();

	private final String baseUrl = "http://localhost:8080/api.book-store/books/";

	@MockBean
	private BookRepository bookRepository;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		bookList.clear();
		authorSet.clear();
		categorySet.clear();
		bookDTOs.clear();

		Author author1 = new Author("1", "Author 1", Set.of());
		Author author2 = new Author("2", "Author 2", Set.of());

		authorSet.add(author1.getId());
		authorSet.add(author2.getId());

		Category category1 = new Category("1", "Category 1", Set.of());
		Category category2 = new Category("1", "Category 1", Set.of());

		categorySet.add(category1.getId());
		categorySet.add(category2.getId());

		Book book1 = new Book("1", "1", "Book 1", "Description", 300, 30, 5, authorSet, categorySet);
		Book book2 = new Book("2", "2", "Book 2", "Description", 450, 38, 15, authorSet, categorySet);

		bookList.add(book1);
		bookList.add(book2);

		bookDTOs.addAll(bookList.stream()
		                        .map(CommonUtils::getBookDtoFrom)
		                        .collect(Collectors.toList()));
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
	void getBookByIdTest_present() {
		Book book = bookList.get(0);
		given(bookRepository.findById(book.getId())).willReturn(Mono.just(book));

		webTestClient.get()
		             .uri(baseUrl + book.getId())
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(book.getId())
		             .jsonPath("$.userId").isEqualTo(book.getUserId())
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription())
		             .jsonPath("$.authorIds").isArray()
		             .jsonPath("$.categoryIds").isArray();

		verify(bookRepository, times(1)).findById(book.getId());
	}

	@Test
//	TODO
	void getBookByIdTest_notPresent() {
		Book book = bookList.get(0);
		given(bookRepository.findById(book.getId())).willReturn(Mono.empty());

		webTestClient.get()
		             .uri(baseUrl + book.getId())
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .isEmpty();

		verify(bookRepository, times(1)).findById(book.getId());
	}

	@Test
	void addNewBookTest_isOk() {
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
		             .jsonPath("$.userId").isEqualTo(book.getUserId())
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription());

		verify(bookRepository, times(1)).save(any());
	}

	@Test
//	TODO
	void integrationTest_addNewBook_error_alreadyExists() {
		Book book = bookList.get(0);
		book.setId(null);
		book.setBookName("New Book without ID");

		given(bookRepository.save(any())).willReturn(Mono.empty());

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
		book.setUserId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		given(bookRepository.save(any())).willReturn(Mono.empty());

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
		book.setUserId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		given(bookRepository.save(any())).willReturn(Mono.empty());

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
	void updateBookTest_present() {
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
		             .jsonPath("$.userId").isEqualTo(book.getUserId())
		             .jsonPath("$.bookName").isEqualTo(book.getBookName())
		             .jsonPath("$.description").isEqualTo(book.getDescription());

		verify(bookRepository, times(1)).save(any());
	}

	@Test
//	TODO
	void integrationTest_updateBook_notPresent() {
		Book book = new Book();
		book.setId("1");
		book.setUserId("1");
		book.setBookName("New Book without ID");
		book.setDescription("Description");
		book.setPages(300);
		book.setChapters(30);
		book.setLastReadChapter(5);

		given(bookRepository.save(any())).willReturn(Mono.empty());

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

	@Test
//	TODO
	void integrationTest_deleteBookById_notPresent() {
		Book book = bookList.get(0);
		given(bookRepository.deleteById(book.getId())).willReturn(Mono.empty());

		webTestClient.delete()
		             .uri(baseUrl + "2")
		             .exchange()
		             .expectStatus().isBadRequest()
		             .expectBody()
		             .consumeWith(System.out::println)
		             .isEmpty();
	}
}
