package com.jacob.bookstore.utils.streams;

import com.jacob.bookstore.models.Author;
import com.jacob.bookstore.models.Book;
import com.jacob.bookstore.repositories.AuthorRepository;
import com.jacob.bookstore.repositories.BookRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BookStream implements ForwardingStream<Book> {

	private       Stream<Book>           stream;
	private       List<Book>             books;
	private final Supplier<Stream<Book>> streamSupplier = () -> this.books.stream();

//	public BookStream(Stream<Book> stream) {
//		this.stream = stream;
//	}

	public BookStream(List<Book> books) {
		this.books = books;
	}

	public BookStream ifNewBook(BookRepository bookRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isPresent()) {
			if (isNewBook(optionalBook.get(), bookRepository)) {
//				return new BookStream(Stream.of(optionalBook.get()));
				return new BookStream(List.of(optionalBook.get()));
			}
		}
//		return new BookStream(Stream.empty());
		return new BookStream(List.of());
	}

	private Optional<Book> getBook() {
		return this.getStream()
		           .findFirst();
	}

	private boolean isNewBook(Book book, BookRepository bookRepository) {
		return bookRepository.findBookByName(book.getName()).isEmpty();
	}

	@Override
	public Stream<Book> getStream() {
//		System.out.println("\nCalled\n");
//		return this.stream;
		return this.streamSupplier.get();
	}

	public BookStream ifNotNewBook(BookRepository bookRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isPresent()) {
			if (isNewBook(optionalBook.get(), bookRepository)) {
//				return new BookStream(Stream.empty());
				return new BookStream(List.of());
			}
//			return new BookStream(Stream.of(optionalBook.get()));
			return new BookStream(List.of(optionalBook.get()));
		}
//		return new BookStream(Stream.empty());
		return new BookStream(List.of());
	}

	public BookStream addNewAuthorsFromBook(AuthorRepository authorRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) {
			//		return new BookStream(Stream.empty());
			return new BookStream(List.of());
		}
		Author.stream(optionalBook.get().getAuthors())
		      .getNewAuthors(authorRepository)
		      .forEach(authorRepository::saveAndFlush);
		return new BookStream(this.books);
	}

	public BookStream updateAuthors(AuthorRepository authorRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) {
			//		return new BookStream(Stream.empty());
			return new BookStream(List.of());
		}
		Book         book    = optionalBook.get();
		List<Author> authors = List.copyOf(book.getAuthors());

		Author.stream(authors)
		      .removeAuthorsFrom(book);

		Author.stream(authors)
		      .addAuthorsTo(book, authorRepository);

//			return new BookStream(Stream.of(book));
		return new BookStream(books);

	}

	public boolean saveBook(BookRepository bookRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isPresent()) {
			bookRepository.saveAndFlush(optionalBook.get());
			return true;
		} else {
			return false;
		}
	}

	public BookStream updateBook(
			Long targetId,
			Book sourceBook,
			BookRepository bookRepository,
			AuthorRepository authorRepository
	) {
		Optional<Book> optionalBook = bookRepository.findById(targetId);
		if (optionalBook.isEmpty()) return new BookStream(List.of());

		Book targetBook = optionalBook.get();

		BeanUtils.copyProperties(sourceBook, targetBook, "id", "authors");

		sourceBook.getAuthors()
		          .stream()
		          .map(Author::getAuthorName)
		          .forEach(System.out::println);

		Author.stream(targetBook.getAuthors())
		      .removeAuthorsFrom(targetBook);
		Author.stream(sourceBook.getAuthors())
		      .addAuthorsTo(targetBook, authorRepository);

		return new BookStream(List.of(targetBook));
	}
}
