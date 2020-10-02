package com.jacob.booktracker.utils.mono;

import com.jacob.booktracker.dtos.CombinedDTO;
import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CombinedDtoMono extends ForwardingMono<CombinedDTO> {

	private final Mono<CombinedDTO> mono;

	public CombinedDtoMono(Mono<CombinedDTO> mono) {
		this.mono = mono;
	}

	@Override
	Mono<CombinedDTO> getMono() {
		return mono;
	}

	public CombinedDtoMono saveAuthors(AuthorRepository authorRepository) {
		return new CombinedDtoMono(
				mono.map(CombinedDTO::getAuthors)
				    .map(authorRepository::saveAll)
				    .flatMap(Flux::collectList)
				    .map(authors -> getIdsAfterAdding(authors, Author::getId))
				    .flatMap(newIds -> addIdsToBookDto(newIds, Book::getAuthorIds))
				    .then(mono)
		);
	}

	private <T> List<String> getIdsAfterAdding(List<T> addedObjects, Function<T, String> getNewIds) {
		return addedObjects.stream()
		                   .map(getNewIds)
		                   .collect(Collectors.toList());
	}

	private Mono<Boolean> addIdsToBookDto(List<String> strings, Function<Book, Set<String>> getOldIds) {
		return mono.map(CombinedDTO::getBook)
		           .map(getOldIds)
		           .map(strings1 -> strings1.addAll(strings));
	}

	public CombinedDtoMono saveCategories(CategoryRepository categoryRepository) {
		return new CombinedDtoMono(
				mono.map(CombinedDTO::getCategories)
				    .map(categoryRepository::saveAll)
				    .flatMap(Flux::collectList)
				    .map(categories -> getIdsAfterAdding(categories, Category::getId))
				    .flatMap(newIds -> addIdsToBookDto(newIds, Book::getCategoryIds))
				    .then(mono)
		);
	}

	public CombinedDtoMono saveBook(BookRepository bookRepository) {
//		mono.map(CombinedDTO::getBook)
//		    .map(book -> book.ge)
		return null;
	}

	public Mono<ServerResponse> getResponseDto() {
		return null;
	}
}
