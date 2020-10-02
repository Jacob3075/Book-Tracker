package com.jacob.booktracker.controllers;

import com.jacob.booktracker.config.MongoReactiveApplication;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.CategoryRepository;
import com.jacob.booktracker.services.CategoryService;
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
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = CategoryController.class)
@ExtendWith(SpringExtension.class)
@Import({CategoryService.class, MongoReactiveApplication.class, CategoryController.class})
class CategoryControllerTest {
	private final List<Category> categoryList = new ArrayList<>();
	private final String      baseUrl    = "http://localhost:8080/api.book-store/categories/";

	@MockBean
	private CategoryRepository categoryRepository;

	@Autowired
	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		Category category1 = new Category();
		Category category2 = new Category();

		category1.setId("1");
		category2.setId("1");

		category1.setCategoryName("Category 1");
		category2.setCategoryName("Category 2");

		category1.setBookIds(Set.of("1", "2", "3"));
		category2.setBookIds(Set.of("1", "2", "3"));

		categoryList.add(category1);
		categoryList.add(category2);
	}

	@Test
	void getAllCategoriesTest() {
		given(categoryRepository.findAll()).willReturn(Flux.fromIterable(categoryList));

		webTestClient.get()
		             .uri(baseUrl)
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$").isArray()
		             .jsonPath("$[0].id").isEqualTo(categoryList.get(0).getId())
		             .jsonPath("$[0].categoryName").isEqualTo(categoryList.get(0).getCategoryName())
		             .jsonPath("$[0].bookIds").isArray();
		
		verify(categoryRepository, times(1)).findAll();
	}

	@Test
	void getCategoryByIdTest() {
		Category category = categoryList.get(0);
		given(categoryRepository.findById(category.getId())).willReturn(Mono.just(category));

		webTestClient.get()
		             .uri(baseUrl + category.getId())
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(category.getId())
		             .jsonPath("$.categoryName").isEqualTo(category.getCategoryName())
		             .jsonPath("$.bookIds").isArray();

		verify(categoryRepository, times(1)).findById(category.getId());
	}

	@Test
	void addNewCategoryTest() {
		Category category = categoryList.get(0);
		given(categoryRepository.save(any())).willReturn(Mono.just(category));

		webTestClient.post()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(category))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(category.getId())
		             .jsonPath("$.categoryName").isEqualTo(category.getCategoryName())
		             .jsonPath("$.bookIds").isArray();

		verify(categoryRepository, times(1)).save(any());
	}

	@Test
	void updateCategoryTest() {
		Category category = categoryList.get(0);
		given(categoryRepository.save(any())).willReturn(Mono.just(category));

		webTestClient.put()
		             .uri(baseUrl)
		             .contentType(MediaType.APPLICATION_JSON)
		             .body(BodyInserters.fromValue(category))
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody()
		             .jsonPath("$.id").isEqualTo(category.getId())
		             .jsonPath("$.categoryName").isEqualTo(category.getCategoryName())
		             .jsonPath("$.bookIds").isArray();

		verify(categoryRepository, times(1)).save(any());
	}

	@Test
	void deleteCategoryTest() {
		Category category = categoryList.get(0);
		given(categoryRepository.deleteById(category.getId())).willReturn(Mono.empty());

		webTestClient.delete()
		             .uri(baseUrl + category.getId())
		             .exchange()
		             .expectStatus().isOk()
		             .expectBody().isEmpty();

		verify(categoryRepository, times(1)).deleteById(category.getId());
	}
}
