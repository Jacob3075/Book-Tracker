package com.jacob.booktracker.utils;

import com.jacob.booktracker.dtos.AuthorDTO;
import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.dtos.CategoryDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public class CommonUtils {

	private static AuthorRepository   authorRepository;
	private static CategoryRepository categoryRepository;

	public static void setAuthorRepository(AuthorRepository authorRepository) {
		CommonUtils.authorRepository = authorRepository;
	}

	public static void setCategoryRepository(CategoryRepository categoryRepository) {
		CommonUtils.categoryRepository = categoryRepository;
	}

	public static BookDTO getBookDtoFrom(Book book) {
		BookDTO.BookDTOBuilder bookDTOBuilder = BookDTO.builder()
		                                               .id(book.getId())
		                                               .userId(book.getUserId())
		                                               .bookName(book.getBookName())
		                                               .description(book.getDescription())
		                                               .pages(book.getPages())
		                                               .chapters(book.getChapters())
		                                               .lastReadChapter(book.getLastReadChapter());

		getAuthorDtoFrom(book.getAuthorIds())
				.subscribe(bookDTOBuilder::authors);
		getCategoryDtoFrom(book.getCategoryIds())
				.subscribe(bookDTOBuilder::categories);

		return bookDTOBuilder.build();
	}

	private static Mono<List<AuthorDTO>> getAuthorDtoFrom(Set<String> authorIds) {

		return authorRepository.findAllById(authorIds)
		                       .map(author -> AuthorDTO.builder()
		                                               .authorName(author.getAuthorName())
		                                               .id(author.getId())
		                                               .build())
		                       .collectList();

	}

	private static Mono<List<CategoryDTO>> getCategoryDtoFrom(Set<String> categoryIds) {
		return categoryRepository.findAllById(categoryIds)
		                         .map(category -> CategoryDTO.builder()
		                                                     .categoryName(category.getCategoryName())
		                                                     .id(category.getId())
		                                                     .build())
		                         .collectList();

	}
}
