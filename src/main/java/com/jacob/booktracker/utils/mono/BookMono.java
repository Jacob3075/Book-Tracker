package com.jacob.booktracker.utils.mono;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.BookRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@NoArgsConstructor
@AllArgsConstructor
public class BookMono extends ForwardingMono<Book> {

	private Mono<Book> mono;

	private BookRepository   bookRepository;

	public BookMono(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public BookMono(Mono<Book> mono) {
		this.mono = mono;
	}

	public Mono<ServerResponse> getResponse() {
		return ok().body(mono, Book.class);
	}

	public BookMono saveBookAuthors() {
//		return new BookMono(this.getMono().doOnNext(book -> authorRepository.saveAll(book.getAuthorIds())));
		return this;
	}

	@Override
	Mono<Book> getMono() {
		return null;
	}

	public BookMono saveBookCategories() {
		return this;
	}

	public BookMono saveBook() {
		return new BookMono(this.getMono().doOnNext(bookRepository::save));
	}
}
