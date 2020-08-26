package com.jacob.booktracker.utils.mono;

import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@NoArgsConstructor
@AllArgsConstructor
public class BookMono extends ForwardingMono<Book> {

	private Mono<Book> mono;

	private BookRepository bookRepository;

	public BookMono(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

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

	public BookMono saveBook() {
		return new BookMono(this.getMono().doOnNext(bookRepository::save));
	}

	@Override
	Mono<Book> getMono() {
		return mono;
	}
}
