package com.jacob.booktracker.utils;

import com.jacob.booktracker.dtos.AuthorDTO;
import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.dtos.CategoryDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonUtils {

//	@Autowired
//	private static BookRepository bookRepository;

	@Autowired
	private static AuthorRepository authorRepository;

	@Autowired
	private static CategoryRepository categoryRepository;

	private CommonUtils() {

	}

	public static BookDTO getBookDtoFrom(Book book) {
		return BookDTO.builder()
		              .id(book.getId())
		              .userId(book.getUserId())
		              .bookName(book.getBookName())
		              .description(book.getDescription())
		              .pages(book.getPages())
		              .chapters(book.getChapters())
		              .lastReadChapter(book.getLastReadChapter())
		              .authors(getAuthorDtoFrom(book.getAuthorIds()))
		              .categories(getCategoryDtoFrom(book.getCategoryIds()))
		              .build();
	}

	private static List<AuthorDTO> getAuthorDtoFrom(Set<String> authorIds) {
		return authorIds.stream()
		                .map(CommonUtils::getAuthorDtoFrom)
		                .collect(Collectors.toList());
	}

	private static List<CategoryDTO> getCategoryDtoFrom(Set<String> categoryIds) {
		return categoryIds.stream()
		                  .map(CommonUtils::getCategoryDtoFrom)
		                  .collect(Collectors.toList());
	}

	private static AuthorDTO getAuthorDtoFrom(String authorId) {
		return authorRepository.findById(authorId)
		                       .map(author -> AuthorDTO.builder()
		                                               .authorName(author.getAuthorName())
		                                               .id(author.getId())
		                                               .build())
		                       .orElse(AuthorDTO.builder().build());
	}

	private static CategoryDTO getCategoryDtoFrom(String categoryId) {
		return categoryRepository.findById(categoryId)
		                         .map(category -> CategoryDTO.builder()
		                                                     .categoryName(category.getCategoryName())
		                                                     .id(category.getId())
		                                                     .build())
		                         .orElse(CategoryDTO.builder().build());
	}
}
