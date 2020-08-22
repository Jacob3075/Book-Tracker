package com.jacob.booktracker.utils.mono;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@NoArgsConstructor
@AllArgsConstructor
public class AuthorMono extends ForwardingMono<Author> {

	private Mono<Author>     authorMono;
	private AuthorRepository authorRepository;

	public AuthorMono(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	public AuthorMono(Mono<Author> authorMono) {
		this.authorMono = authorMono;
	}

//	public AuthorStream updateAuthor(Mono<Author> oldAuthorMono) {
//		return new AuthorMono(authorMono.doOnNext(newAuthor -> {
//			                                      oldAuthorMono.subscribe(oldAuthor -> {
//				                                      BeanUtils.copyProperties(newAuthor, oldAuthor, "id");
//				                                      authorRepository.save(oldAuthor);
//			                                      });
//			                                      return newAuthor;
//		                                      }
//		));
//	}

	/*
	 * */
	public AuthorMono updateAuthor(Mono<Author> oldAuthorMono) {
		return new AuthorMono(authorMono.doOnNext(
				newAuthor -> oldAuthorMono.flatMap(
						oldAuthor -> {
							BeanUtils.copyProperties(newAuthor, oldAuthor, "id");
							return authorRepository.save(oldAuthor);
						}).subscribe())
		);

//		authorMono.doOnNext(
//				newAuthor -> oldAuthorMono.flatMap(
//						oldAuthor -> {
//							BeanUtils.copyProperties(newAuthor, oldAuthor, "id");
//							return authorRepository.save(oldAuthor);
//						}).subscribe());
	}

	public AuthorMono saveAuthor() {
		return null;
	}

	public Mono<ServerResponse> toServerResponse() {
		return null;
	}

	@Override
	Mono<Author> getMono() {
		return authorMono;
	}

	@Override
	public void subscribe(CoreSubscriber<? super Author> actual) {
		getMono().subscribe(actual);
	}


}
