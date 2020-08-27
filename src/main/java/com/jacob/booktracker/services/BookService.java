package com.jacob.booktracker.services;

import com.jacob.booktracker.dtos.BookDTO;
import com.jacob.booktracker.dtos.CombinedDTO;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import com.jacob.booktracker.utils.CommonUtils;
import com.jacob.booktracker.utils.mono.CombinedDtoMono;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
public class BookService {

	private final BookRepository     bookRepository;
	private final AuthorRepository   authorRepository;
	private final CategoryRepository categoryRepository;

	public BookService(
			BookRepository bookRepository,
			AuthorRepository authorRepository,
			CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
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
		Mono<CombinedDTO> combinedDTOMono = serverRequest.bodyToMono(BookDTO.class)
		                                                 .map(CommonUtils::getCombinedDto);
		new CombinedDtoMono(combinedDTOMono)
				.saveAuthors(authorRepository)
				.saveCategories(categoryRepository)
				.saveBook(bookRepository)
				.getResponseDto();


//		serverRequest.bodyToMono(BookDTO.class)
//		             .map(CommonUtils::getCombinedDto)
//		             .map(combinedDTO -> {
//			             Book.mono(Mono.just(combinedDTO.getBook()))
//			                 .checkIfBookAlreadyExists(bookRepository)
//			                 .saveBook(bookRepository)
//			                 .getResponseDto();
//		             })

		return Book.mono(serverRequest.bodyToMono(Book.class))
		           .checkIfBookAlreadyExists(bookRepository)
		           .saveBook(bookRepository)
		           .getResponseDto();

//		return ok().body(
//				serverRequest.bodyToMono(Book.class)
//				             .flatMap(bookRepository::save),
//				Book.class
//		);
	}
}
