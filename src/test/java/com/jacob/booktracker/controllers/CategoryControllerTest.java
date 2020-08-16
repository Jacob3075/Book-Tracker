package com.jacob.booktracker.controllers;

import com.google.gson.Gson;
import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.services.CategoryService;
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

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

	private final List<CategoryDTO> categoryList = new ArrayList<>();
	private final String            urlTemplate  = "/api.book-store/categories/";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {
		categoryList.clear();

		categoryList.addAll(Arrays.asList(
				new CategoryDTO("1", "Category 1"),
				new CategoryDTO("2L", "Category 2"),
				new CategoryDTO("3L", "Category 3"),
				new CategoryDTO("4L", "Category 4"),
				new CategoryDTO("5L", "Category 4")
		));


	}

	@Test
	void getAllCategories() throws Exception {

		given(categoryService.findAll()).willReturn(categoryList);

		mockMvc.perform(get(urlTemplate))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.size()", is(categoryList.size())))
		       .andExpect(jsonPath("$[0].categoryName", is(categoryList.get(0).getCategoryName())));

	}

	@Test
	void findById() throws Exception {
		given(categoryService.findById("1L")).willReturn(Optional.ofNullable(categoryList.get(0)));
		given(categoryService.findById("2L")).willReturn(Optional.empty());

		mockMvc.perform(get(urlTemplate + "1"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.categoryName", is(categoryList.get(0).getCategoryName())))
		       .andExpect(jsonPath("$.id", is(categoryList.get(0).getId())));

		mockMvc.perform(get(urlTemplate + "2"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$").doesNotExist());

	}

	@Test
	void addNewCategory() throws Exception {
		Category category = CommonUtils.convertFromCategoryDTO(categoryList.get(1));

		given(categoryService.addNewCategory(category)).willReturn(true);

		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON)
		                                 .content(new Gson().toJson(category)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", is("CREATED")));

		given(categoryService.addNewCategory(category)).willReturn(false);

		mockMvc.perform(post(urlTemplate).contentType(MediaType.APPLICATION_JSON)
		                                 .content(new Gson().toJson(category)))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$", is("NOT FOUND")));
	}

	@Test
	void deleteCategory() throws Exception {

		given(categoryService.deleteById("1L")).willReturn(true);
		given(categoryService.deleteById("2L")).willReturn(false);

		mockMvc.perform(delete(urlTemplate + "1"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", is("DELETED")));

		mockMvc.perform(delete(urlTemplate + "2"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$", is("NOT FOUND")));


	}

	@Test
	void updateCategory() {
	}
}
