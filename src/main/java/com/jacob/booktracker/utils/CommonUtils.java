package com.jacob.booktracker.utils;

import com.jacob.booktracker.dtos.AuthorDTO;
import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.dtos.CategoryDTO;
import com.jacob.booktracker.dtos.CombinedDTO;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonUtils {

	private static AuthorRepository   authorRepository;
	private static CategoryRepository categoryRepository;

	private CommonUtils() {
	}

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

	public static CombinedDTO getCombinedDto(BookDTO bookDTO) {
		bookDTO.setId(ObjectId.get().toString());

		CombinedDTO combinedDTO = CombinedDTO.builder()
		                                     .book(getBookFromDto(bookDTO))
		                                     .authors(bookDTO.getAuthors()
		                                                     .stream()
		                                                     .map(CommonUtils::getAuthorFromDto)
		                                                     .collect(Collectors.toList()))
		                                     .categories(bookDTO.getCategories()
		                                                        .stream()
		                                                        .map(CommonUtils::getCategoryFromDto)
		                                                        .collect(Collectors.toList()))
		                                     .build();
		combinedDTO.getAuthors()
		           .forEach(author -> author.getBookIds().add(bookDTO.getId()));
		combinedDTO.getCategories()
		           .forEach(category -> category.getBookIds().add(bookDTO.getId()));
		return combinedDTO;
	}

	private static Book getBookFromDto(BookDTO bookDTO) {
		return Book.builder()
		           .id(bookDTO.getId())
		           .userId(bookDTO.getUserId())
		           .bookName(bookDTO.getBookName())
		           .description(bookDTO.getDescription())
		           .pages(bookDTO.getPages())
		           .chapters(bookDTO.getChapters())
		           .lastReadChapter(bookDTO.getLastReadChapter())
		           .authorIds(bookDTO.getAuthors()
		                             .stream()
		                             .map(AuthorDTO::getId)
		                             .collect(Collectors.toSet()))
		           .categoryIds(bookDTO.getCategories()
		                               .stream()
		                               .map(CategoryDTO::getId)
		                               .collect(Collectors.toSet()))
		           .build();
	}

	private static Author getAuthorFromDto(AuthorDTO authorDTO) {
		return Author.builder()
		             .id(authorDTO.getId())
		             .authorName(authorDTO.getAuthorName())
		             .build();
	}

	private static Category getCategoryFromDto(CategoryDTO categoryDTO) {
		return Category.builder()
		               .id(categoryDTO.getId())
		               .categoryName(categoryDTO.getCategoryName())
		               .build();
	}
}
