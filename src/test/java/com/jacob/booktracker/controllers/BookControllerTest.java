package com.jacob.booktracker.controllers;

import com.google.gson.Gson;
import com.jacob.booktracker.dtos.response.AuthorDTO;
import com.jacob.booktracker.dtos.response.BookDTO;
import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.services.BookService;
import com.jacob.booktracker.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

	private final List<BookDTO>     bookList     = new ArrayList<>();
	private final List<AuthorDTO>   authorList   = new ArrayList<>();
	private final List<CategoryDTO> categoryList = new ArrayList<>();
	private final String            urlTemplate  = "/api.book-store/books/";
	@Autowired
	private       MockMvc           mockMvc;
	@MockBean
	private       BookService       bookService;

	@BeforeEach
	void setUp() {
		authorList.clear();
		categoryList.clear();
		bookList.clear();

		authorList.add(new AuthorDTO(1L, "Name"));

		categoryList.add(new CategoryDTO(1L, "Category"));

		bookList.addAll(Arrays.asList(
				new BookDTO(1L, "Book1", authorList, categoryList, 300, 28, 18),
				new BookDTO(2L, "Book2", authorList, categoryList, 200, 20, 4)
		));
	}

	@Test
	void getAllBooks() throws Exception {
		given(bookService.findAll()).willReturn(bookList);

		mockMvc.perform(get(urlTemplate))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.size()", is(bookList.size())))
		       .andExpect(jsonPath("$[0].name", is(bookList.get(0).getName())))
		       .andExpect(jsonPath("$[0].authors[0].authorName", is(authorList.get(0).getAuthorName())))
		       .andExpect(jsonPath("$[0].authors[0].id", is(authorList.get(0).getId().intValue())));

	}

	@Test
	void getBookById() throws Exception {
		given(bookService.findById(1L)).willReturn(Optional.ofNullable(bookList.get(0)));
		given(bookService.findById(2L)).willReturn(Optional.empty());

		mockMvc.perform(get(urlTemplate + "1"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.name", is(bookList.get(0).getName())))
		       .andExpect(jsonPath("$.id", is(bookList.get(0).getId().intValue())))
		       .andExpect(jsonPath("$.pages", is(bookList.get(0).getPages())))
		       .andExpect(jsonPath("$.chapters", is(bookList.get(0).getChapters())))
		       .andExpect(jsonPath("$.lastReadChapter", is(bookList.get(0).getLastReadChapter())))
		       .andExpect(jsonPath("$.authors.size()", is(authorList.size())))
		       .andExpect(jsonPath("$.authors[0].id", is(authorList.get(0).getId().intValue())))
		       .andExpect(jsonPath("$.authors[0].authorName", is(authorList.get(0).getAuthorName())))
		       .andExpect(jsonPath("$.categories.size()", is(categoryList.size())))
		       .andExpect(jsonPath("$.categories[0].id", is(categoryList.get(0).getId().intValue())))
		       .andExpect(jsonPath("$.categories[0].categoryName", is(categoryList.get(0).getCategoryName())));

		mockMvc.perform(get(urlTemplate + "2"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	void deleteBookById() throws Exception {
		given(bookService.deleteById(1L)).willReturn(true);
		given(bookService.deleteById(2L)).willReturn(false);

		mockMvc.perform(delete(urlTemplate + "1"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", is("DELETED")));

		mockMvc.perform(delete(urlTemplate + "2"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$", is("NOT FOUND")));

	}

	@Test
	void updateBookById() throws Exception {

		Book book = CommonUtils.convertFromBookDTO(bookList.get(1));

		given(bookService.updateBook(1L, book)).willReturn(true);
		given(bookService.updateBook(2L, book)).willReturn(false);

		mockMvc.perform(put(urlTemplate + "1").contentType(MediaType.APPLICATION_JSON)
		                                      .content(new Gson().toJson(book)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", is("UPDATED")));

		mockMvc.perform(put(urlTemplate + "2").contentType(MediaType.APPLICATION_JSON)
		                                      .content(new Gson().toJson(book)))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$", is("NOT FOUND")));


	}

	@Test
	void addNewBook() throws Exception {
		Book book = CommonUtils.convertFromBookDTO(bookList.get(1));

		given(bookService.addNewBook(book)).willReturn(true);

		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON)
		                                 .content(new Gson().toJson(book)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", is("CREATED")));

		given(bookService.addNewBook(book)).willReturn(false);

		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON)
		                                 .content(new Gson().toJson(book)))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$", is("NOT FOUND")));
	}

	@Test
	void updateLastReadChapter() throws Exception {

		given(bookService.updateChapter(1L, 10)).willReturn(true);
		given(bookService.updateChapter(2L, 10)).willReturn(false);

		mockMvc.perform(post(urlTemplate + "1/last-read-chapter=10"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", is("UPDATED")));

		mockMvc.perform(post(urlTemplate + "2/last-read-chapter=10"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$", is("NOT FOUND")));

	}
}
