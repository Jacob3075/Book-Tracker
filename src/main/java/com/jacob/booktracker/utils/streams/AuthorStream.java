package com.jacob.booktracker.utils.streams;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.repositories.AuthorRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorStream implements ForwardingStream<Author> {

	private       List<Author>             authors;
	private final Supplier<Stream<Author>> streamSupplier = () -> this.authors.stream();

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
		return authorRepository.findAuthorByAuthorName(author.getAuthorName()).isEmpty();
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

	public void addAuthorsTo(Book book, AuthorRepository authorRepository) {
		this.getStream()
		    .map(Author::getAuthorName)
		    .forEach(authorName -> authorRepository.findAuthorByAuthorName(authorName)
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
		if (optionalAuthor.isEmpty()) return new AuthorStream(List.of());

		if (isNewAuthor(optionalAuthor.get(), authorRepository)) {
			return new AuthorStream(this.authors);
		}
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
			return new AuthorStream(List.of(oldAuthor));
		}
		return new AuthorStream(List.of());
	}
}
