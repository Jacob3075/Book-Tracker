package com.jacob.booktracker.utils.mono;

import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.utils.CommonUtils;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@NoArgsConstructor
public class BookMono extends ForwardingMono<Book> {

	private Mono<Book> mono;

	public BookMono(Mono<Book> mono) {
		this.mono = mono;
	}

	public Mono<ServerResponse> getResponseDto() {
		return mono.map(CommonUtils::getBookDtoFrom)
		           .flatMap(bookDTO -> ok().body(Mono.just(bookDTO), BookDTO.class))
		           .switchIfEmpty(badRequest().build());
	}

	public BookMono saveBookAuthors() {
//		return new BookMono(this.getMono().doOnNext(book -> authorRepository.saveAll(book.getAuthorIds())));
		return this;
	}

	public BookMono saveBookCategories() {
		return this;
	}

	public BookMono saveBook(BookRepository bookRepository) {
		return new BookMono(this.getMono().doOnNext(bookRepository::save));
	}

	@Override
	Mono<Book> getMono() {
		return mono;
	}

	public BookMono checkIfBookAlreadyExists(BookRepository bookRepository) {
		return Book.mono(mono.flatMap(book -> getBookMono(bookRepository, book)));
	}

	private Mono<Book> getBookMono(BookRepository bookRepository, Book book) {
		return bookRepository.existsByBookName(book.getBookName())
		                     .flatMap(this::apply);
	}

	private Mono<Book> apply(boolean aBoolean) {
		if (aBoolean) {
			return Mono.empty();
		} else {
			return mono;
		}
	}
}
