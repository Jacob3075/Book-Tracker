package com.jacob.booktracker.utils;

import com.jacob.booktracker.dtos.response.AuthorDTO;
import com.jacob.booktracker.dtos.response.BookDTO;
import com.jacob.booktracker.dtos.response.CategoryDTO;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;

import java.util.Optional;
import java.util.stream.Collectors;

public class CommonUtils {

	public static Optional<BookDTO> convertToBookDTO(Optional<Book> optionalBook) {
		return optionalBook.map(CommonUtils::convertToBookDTO);
	}

	public static BookDTO convertToBookDTO(Book book) {
		BookDTO bookDTO = new BookDTO();

		bookDTO.setId(book.getId());
		bookDTO.setName(book.getName());
		bookDTO.setChapters(book.getChapters());
		bookDTO.setPages(book.getPages());
		bookDTO.setLastReadChapter(book.getLastReadChapter());

		bookDTO.setAuthors(book.getAuthors()
		                       .stream()
		                       .map(CommonUtils::convertToAuthorDTO)
		                       .collect(Collectors.toList()));

		bookDTO.setCategories(book.getCategories()
		                          .stream()
		                          .map(CommonUtils::convertToCategoryDTO)
		                          .collect(Collectors.toList()));
		return bookDTO;
	}

	public static AuthorDTO convertToAuthorDTO(Author author) {
		AuthorDTO authorDTO = new AuthorDTO();

		authorDTO.setId(author.getId());
		authorDTO.setAuthorName(author.getAuthorName());

		return authorDTO;
	}

	public static CategoryDTO convertToCategoryDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();

		categoryDTO.setId(category.getId());
		categoryDTO.setCategoryName(category.getCategoryName());

		return null;
	}

	public static Book convertFromBookDTO(BookDTO bookDTO) {
		Book book = new Book();

		book.setAuthors(bookDTO.getAuthors()
		                       .stream()
		                       .map(CommonUtils::convertFromAuthorDTO)
		                       .collect(Collectors.toList()));
		book.setCategories(bookDTO.getCategories()
		                          .stream()
		                          .map(CommonUtils::convertFromCategoryDTO)
		                          .collect(Collectors.toList()));
		book.setName(bookDTO.getName());
		book.setChapters(bookDTO.getChapters());
		book.setPages(bookDTO.getPages());
		book.setLastReadChapter(bookDTO.getLastReadChapter());
		book.setDescription("");

		return book;
	}

	public static Author convertFromAuthorDTO(AuthorDTO authorDTO) {
		Author author = new Author();

		author.setId(authorDTO.getId());
		author.setAuthorName(authorDTO.getAuthorName());

		return author;
	}

	public static Category convertFromCategoryDTO(CategoryDTO categoryDTO) {
		Category category = new Category();

		category.setId(categoryDTO.getId());
		category.setCategoryName(categoryDTO.getCategoryName());

		return null;
	}


}
