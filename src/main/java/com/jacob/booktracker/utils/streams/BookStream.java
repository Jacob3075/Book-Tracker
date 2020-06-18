package com.jacob.booktracker.utils.streams;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.BookRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BookStream implements ForwardingStream<Book> {

	private       List<Book>             books;
	private final Supplier<Stream<Book>> streamSupplier = () -> this.books.stream();

	public BookStream(List<Book> books) {
		this.books = books;
	}

	public BookStream ifNewBook(BookRepository bookRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) return new BookStream(List.of());

		if (isNewBook(optionalBook.get(), bookRepository)) {
			return new BookStream(List.of(optionalBook.get()));
		}
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
		return this.streamSupplier.get();
	}

	public BookStream ifNotNewBook(BookRepository bookRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) return new BookStream(List.of());

		if (isNewBook(optionalBook.get(), bookRepository)) {
			return new BookStream(List.of());
		}
		return new BookStream(List.of(optionalBook.get()));
	}

	public BookStream addNewAuthorsFromBook(AuthorRepository authorRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) return new BookStream(List.of());
		Author.stream(optionalBook.get().getAuthors())
		      .getNewAuthors(authorRepository)
		      .forEach(authorRepository::saveAndFlush);
		return new BookStream(this.books);
	}

	public BookStream updateAuthors(AuthorRepository authorRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) return new BookStream(List.of());
		Book         book    = optionalBook.get();
		List<Author> authors = List.copyOf(book.getAuthors());

		Author.stream(authors)
		      .removeAuthorsFrom(book);

		Author.stream(authors)
		      .addAuthorsTo(book, authorRepository);

		return new BookStream(this.books);

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
			BookRepository bookRepository,
			AuthorRepository authorRepository,
			CategoryRepository categoryRepository) {

		Optional<Book> optionalBook = bookRepository.findById(targetId);
		if (optionalBook.isEmpty()) return new BookStream(List.of());

		Optional<Book> optionalSourceBook = this.getBook();
		if (optionalSourceBook.isEmpty()) return new BookStream(List.of());

		Book targetBook = optionalBook.get();
		Book sourceBook = optionalSourceBook.get();
		BeanUtils.copyProperties(sourceBook, targetBook, "id", "authors", "categories");

		Author.stream(targetBook.getAuthors())
		      .removeAuthorsFrom(targetBook);
		Author.stream(sourceBook.getAuthors())
		      .addAuthorsTo(targetBook, authorRepository);

		Category.stream(targetBook.getCategories())
		        .removeCategoriesFrom(targetBook);
		Category.stream(sourceBook.getCategories())
		        .addCategoriesTo(targetBook, categoryRepository);

		return new BookStream(List.of(targetBook));
	}

	public BookStream addNewCategoriesFromBook(CategoryRepository categoryRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) return new BookStream(List.of());

		Book book = optionalBook.get();
		Category.stream(book.getCategories())
		        .getNewCategories(categoryRepository)
		        .forEach(categoryRepository::saveAndFlush);

		return new BookStream(this.books);
	}

	public BookStream updateCategories(CategoryRepository categoryRepository) {
		Optional<Book> optionalBook = this.getBook();
		if (optionalBook.isEmpty()) return new BookStream(List.of());

		Book           book       = optionalBook.get();
		List<Category> categories = List.copyOf(book.getCategories());

		Category.stream(categories)
		        .removeCategoriesFrom(book);

		Category.stream(categories)
		        .addCategoriesTo(book, categoryRepository);

		return new BookStream(this.books);
	}
}
