package com.jacob.booktracker.controllers;

import com.jacob.booktracker.config.MongoReactiveApplication;
import com.jacob.booktracker.dtos.AuthorDTO;
import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.dtos.CategoryDTO;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import com.jacob.booktracker.services.BookService;
import com.jacob.booktracker.utils.CommonUtils;
import com.jacob.booktracker.utils.StaticContextInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = BookController.class)
@ExtendWith(SpringExtension.class)
@Import({BookService.class, BookController.class, MongoReactiveApplication.class, CommonUtils.class,
		StaticContextInitializer.class, BookDTO.class, AuthorDTO.class, CategoryDTO.class})
class BookControllerTest {

	private static final List<BookDTO> bookDTOs = new ArrayList<>();
	private static final List<Book>    books    = new ArrayList<>();
	private final        String        baseUrl  = "http://localhost:8080/api.book-store/books/";

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private CategoryRepository categoryRepository;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeAll
	static void beforeAll() {
		bookDTOs.clear();

		Book book1 = new Book("1", "1", "Book 1", "Description", 300, 30, 5, Set.of("1", "2"), Set.of("1", "2"));

		BookDTO bookDTO = BookDTO.builder()
		                         .id(book1.getId())
		                         .userId(book1.getUserId())
		                         .bookName(book1.getBookName())
		                         .description(book1.getDescription())
		                         .pages(book1.getPages())
		                         .chapters(book1.getChapters())
		                         .lastReadChapter(book1.getLastReadChapter())
		                         .categories(List.of(CategoryDTO.builder()
		                                                        .categoryName("Cat 1")
		                                                        .id("1")
		                                                        .build()))
		                         .authors(List.of(AuthorDTO.builder()
		                                                   .authorName("Author 1")
		                                                   .id("1")
		                                                   .build()))
		                         .build();

		books.add(book1);
		books.add(book1);

		bookDTOs.add(bookDTO);
		bookDTOs.add(bookDTO);
	}

	@Nested
	@DisplayName("Tests to get all books and books by ID")
	class GetBooksTest {
		@Test
		void getAllBooksTest() {
			BookDTO bookDTO = bookDTOs.get(0);
			given(bookRepository.findAll()).willReturn(Flux.fromIterable(books));
			given(categoryRepository.findById("1")).willReturn(Mono.just(new Category("1", "Cat 1", Set.of())));
			given(authorRepository.findById("1")).willReturn(Mono.just(new Author("1", "Author 1", Set.of())));

			webTestClient.get()
			             .uri(baseUrl)
			             .exchange()
			             .expectStatus().isOk()
			             .expectBody()
			             .consumeWith(System.out::println)
			             .jsonPath("$").isArray()
			             .jsonPath("$[0].id").isEqualTo(bookDTO.getId())
			             .jsonPath("$[0].userId").isEqualTo(bookDTO.getUserId())
			             .jsonPath("$[0].bookName").isEqualTo(bookDTO.getBookName())
			             .jsonPath("$[0].description").isEqualTo(bookDTO.getDescription())
			             .jsonPath("$[0].pages").isEqualTo(bookDTO.getPages())
			             .jsonPath("$[0].chapters").isEqualTo(bookDTO.getChapters())
			             .jsonPath("$[0].lastReadChapter").isEqualTo(bookDTO.getLastReadChapter())
			             .jsonPath("$[0].authors").isArray()
			             .jsonPath("$[0].authors[0].authorName")
			             .isEqualTo(bookDTO.getAuthors().get(0).getAuthorName())
			             .jsonPath("$[0].categories").isArray()
			             .jsonPath("$[0].categories[0].categoryName")
			             .isEqualTo(bookDTO.getCategories().get(0).getCategoryName());
		}

		@Test
		void getBookByIdTest() {
			BookDTO bookDTO = bookDTOs.get(0);
			given(bookRepository.findById("1")).willReturn(Mono.just(books.get(0)));
			given(categoryRepository.findAllById(anySet()))
					.willReturn(Flux.just(new Category("1", "Cat 1", Set.of()
					)));
			given(authorRepository.findAllById(anySet()))
					.willReturn(Flux.just(new Author("1", "Author 1", Set.of()
					)));
//			MockedStatic mockedStatic;

			webTestClient.get()
			             .uri(baseUrl + "1")
			             .exchange()
			             .expectStatus().isOk()
			             .expectBody()
			             .consumeWith(System.out::println)
			             .jsonPath("$.id").isEqualTo(bookDTO.getId())
			             .jsonPath("$.userId").isEqualTo(bookDTO.getUserId())
			             .jsonPath("$.bookName").isEqualTo(bookDTO.getBookName())
			             .jsonPath("$.description").isEqualTo(bookDTO.getDescription())
			             .jsonPath("$.pages").isEqualTo(bookDTO.getPages())
			             .jsonPath("$.chapters").isEqualTo(bookDTO.getChapters())
			             .jsonPath("$.lastReadChapter").isEqualTo(bookDTO.getLastReadChapter())
			             .jsonPath("$.authors").isArray()
			             .jsonPath("$.authors[0].authorName").isEqualTo(bookDTO.getAuthors().get(0).getAuthorName())
			             .jsonPath("$.categories").isArray()
			             .jsonPath("$.categories[0].categoryName")
			             .isEqualTo(bookDTO.getCategories().get(0).getCategoryName());
		}

		@Test
		void getBookById_notPresent() {
			given(bookRepository.findById("1")).willReturn(Mono.empty());
			given(categoryRepository.findById("1")).willReturn(Mono.empty());
			given(authorRepository.findById("1")).willReturn(Mono.empty());

			webTestClient.get()
			             .uri(baseUrl + "1")
			             .exchange()
//			             .expectStatus().isBadRequest()
			             .expectBody()
			             .consumeWith(System.out::println)
			             .isEmpty();
		}
	}

	@Nested
	@DisplayName("Tests to add new book")
	class AddNewBookTests {
		@Test
		void addNewBookTest() {
			BookDTO bookDTO = bookDTOs.get(0);
			given(bookRepository.save(any())).willReturn(Mono.just(books.get(0)));
			given(categoryRepository.findById("1")).willReturn(Mono.just(new Category("1", "Cat 1", Set.of())));
			given(authorRepository.findById("1")).willReturn(Mono.just(new Author("1", "Author 1", Set.of())));

			webTestClient.post()
			             .uri(baseUrl)
			             .contentType(MediaType.APPLICATION_JSON)
			             .body(BodyInserters.fromValue(bookDTO))
			             .exchange()
			             .expectStatus().isOk()
			             .expectBody()
			             .consumeWith(System.out::println)
			             .jsonPath("$.id").isEqualTo(bookDTO.getId())
			             .jsonPath("$.userId").isEqualTo(bookDTO.getUserId())
			             .jsonPath("$.bookName").isEqualTo(bookDTO.getBookName())
			             .jsonPath("$.description").isEqualTo(bookDTO.getDescription())
			             .jsonPath("$.authors").isArray()
			             .jsonPath("$.categories").isArray();
		}
	}

}
