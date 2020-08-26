package com.jacob.booktracker.utils;

import com.jacob.booktracker.dtos.AuthorDTO;
import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.dtos.CategoryDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CommonUtils {

//	@Autowired
//	private static BookRepository bookRepository;

	private AuthorRepository authorRepository;

	private CategoryRepository categoryRepository;

	public CommonUtils(
			AuthorRepository authorRepository,
			CategoryRepository categoryRepository) {
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
	}

	public BookDTO getBookDtoFrom(Book book) {
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

	private List<AuthorDTO> getAuthorDtoFrom(Set<String> authorIds) {

		return authorRepository.findAllById(authorIds)
		                       .map(author -> AuthorDTO.builder()
		                                               .authorName(author.getAuthorName())
		                                               .id(author.getId())
		                                               .build())
		                       .collectList()
		                       .block();

	}

	private List<CategoryDTO> getCategoryDtoFrom(Set<String> categoryIds) {
		return categoryRepository.findAllById(categoryIds)
		                         .map(category -> CategoryDTO.builder()
		                                                             .categoryName(category.getCategoryName())
		                                                             .id(category.getId())
		                                                             .build())
		                         .collectList()
		                         .block();

	}
}
