package com.jacob.booktracker.utils.mono;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.BookRepository;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@NoArgsConstructor
@AutoConfigureAfter
public class BookMono extends ForwardingMono<Book> {

	private Mono<Book> bookMono;

	private BookRepository   bookRepository;
	private AuthorRepository authorRepository;

	public BookMono(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	public BookMono(Mono<Book> bookMono) {
		this.bookMono = bookMono;
	}

	public Mono<ServerResponse> getResponse() {
		return ok().body(bookMono, Book.class);
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
