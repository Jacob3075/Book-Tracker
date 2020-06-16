package com.jacob.bookstore.utils.streams;

import com.jacob.bookstore.models.Author;
import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.repositories.AuthorRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorStream implements ForwardingStream<Author> {

	//	private Stream<Author> stream;
	private       List<Author>             authors;
	private final Supplier<Stream<Author>> streamSupplier = () -> this.authors.stream();

//	public AuthorStream(Stream<Author> stream) {
//		this.stream = stream;
//	}

	public AuthorStream(List<Author> authors) {
		this.authors = authors;
	}

	public AuthorStream getOldAuthors(AuthorRepository authorRepository) {
		return new AuthorStream(
				this.getStream()
				    .filter(Predicate.not(
						    author -> isNewAuthor(author, authorRepository)
				    ))
				    .collect(Collectors.toList())
		);
	}

	@Override
	public Stream<Author> getStream() {
//		return this.stream;
		return this.streamSupplier.get();
	}

	private boolean isNewAuthor(Author author, AuthorRepository authorRepository) {
		return authorRepository.findAuthorsByAuthorName(author.getAuthorName()).isEmpty();
	}

	public AuthorStream getNewAuthors(AuthorRepository authorRepository) {
		return new AuthorStream(
				this.getStream()
				    .filter(author -> isNewAuthor(author, authorRepository))
				    .collect(Collectors.toList())
		);
	}

	public AuthorStream getAuthorsNotIn(Book book) {
		List<String> authorNames = Author.stream(book.getAuthors())
		                                 .getAuthorNames();

		return new AuthorStream(this.getStream()
		                            .filter(author -> !authorNames.contains(author.getAuthorName()))
		                            .collect(Collectors.toList())
		);
	}

	public List<String> getAuthorNames() {
		return this.getStream()
		           .map(Author::getAuthorName)
		           .collect(Collectors.toList());
	}

	public void addAuthorsTo(Book book, AuthorRepository repository) {
		this.getStream()
		    .map(Author::getAuthorName)
		    .forEach(authorName -> repository.findAuthorByAuthorName(authorName)
		                                     .ifPresent(author -> book.getAuthors().add(author))
		    );
	}

	public void removeAuthorsFrom(Book book) {
		List<Author> authorsToRemove = this.getStream()
		                                   .collect(Collectors.toList());

		book.getAuthors().removeIf(authorsToRemove::contains);
	}

	public AuthorStream ifNewAuthor(AuthorRepository authorRepository) {
		Optional<Author> optionalAuthor = this.getAuthor();
		if (optionalAuthor.isPresent()) {
			if (isNewAuthor(optionalAuthor.get(), authorRepository)) {
//				return new AuthorStream(this.getStream());
				return new AuthorStream(this.authors);
			}
		}
//		return new AuthorStream(Stream.empty());
		return new AuthorStream(List.of());
	}

	private Optional<Author> getAuthor() {
		return this.getStream()
		           .findFirst();
	}

	public boolean saveAuthor(AuthorRepository authorRepository) {
		Optional<Author> optionalAuthor = this.getAuthor();
		if (optionalAuthor.isPresent()) {
			authorRepository.saveAndFlush(optionalAuthor.get());
			return true;
		}
		return false;
	}

	public AuthorStream updateAuthor(Long id, AuthorRepository authorRepository) {
		Optional<Author> optionalOldAuthor = authorRepository.findById(id);
		Optional<Author> optionalNewAuthor = this.getAuthor();
		if (optionalOldAuthor.isPresent() && optionalNewAuthor.isPresent()) {
			Author oldAuthor = optionalOldAuthor.get();
			Author newAuthor = optionalNewAuthor.get();
			BeanUtils.copyProperties(newAuthor, oldAuthor, "id");
//			return new AuthorStream(Stream.of(oldAuthor));
			return new AuthorStream(List.of(oldAuthor));
		}
//		return new AuthorStream(Stream.empty());
		return new AuthorStream(List.of());
	}
}