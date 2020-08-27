package com.jacob.booktracker.models;

import com.jacob.booktracker.utils.mono.BookMono;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "Books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

	@Id
	private String id;
	private String userId;

	@NotEmpty
	private String bookName;
	@NotEmpty
	private String description;

	private int pages;
	private int chapters;

	private int lastReadChapter;

	private Set<String> authorIds = new HashSet<>();
	private Set<String> categoryIds = new HashSet<>();

	public static BookMono mono(Mono<Book> bookMono) {
		return new BookMono(bookMono);
	}
}
