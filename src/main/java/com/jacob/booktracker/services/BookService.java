package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(
			BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
		return ok().body(bookRepository.findAll(), Book.class);
	}

	public Mono<ServerResponse> findById(ServerRequest serverRequest) {
		return Book.mono(bookRepository.findById(serverRequest.pathVariable("id")))
		           .getResponseDto();
	}

	public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
		return ok().body(bookRepository.deleteById(serverRequest.pathVariable("id")), Void.class);
	}

	public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
		return addNewBook(serverRequest);
	}

	public Mono<ServerResponse> addNewBook(ServerRequest serverRequest) {
		return ok().body(
				serverRequest.bodyToMono(Book.class)
				             .flatMap(bookRepository::save),
				Book.class
		);
	}
}
