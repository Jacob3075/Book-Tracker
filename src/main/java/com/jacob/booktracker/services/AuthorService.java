package com.jacob.booktracker.services;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class AuthorService {

	private final AuthorRepository authorRepository;

	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
		return ok().body(authorRepository.findAll(), Author.class);
	}

	public Mono<ServerResponse> findById(ServerRequest serverRequest) {
		return ok().body(authorRepository.findById(serverRequest.pathVariable("id")), Author.class);
	}

	public Mono<ServerResponse> addNewAuthor(ServerRequest serverRequest) {
		return ok().body(serverRequest.bodyToMono(Author.class)
		                              .flatMap(authorRepository::save), Author.class);
	}

	public Mono<ServerResponse> updateAuthor(ServerRequest serverRequest) {
		return ok().body(serverRequest.bodyToMono(Author.class)
		                              .flatMap(authorRepository::save), Author.class);
	}

	public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
		return ok().body(authorRepository.deleteById(serverRequest.pathVariable("id")), Void.class);
	}
}
