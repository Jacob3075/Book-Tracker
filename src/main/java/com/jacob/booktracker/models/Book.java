package com.jacob.booktracker.models;

import com.jacob.booktracker.utils.streams.BookStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

	@Id
	private String id;

	@NotEmpty
	private String name;
	@NotEmpty
	private String description;

	private int pages;
	private int chapters;

	private int lastReadChapter;

	private List<Author> authors = new ArrayList<>();
//	@JsonIgnoreProperties("books")
//	private Set<Author> authors = new HashSet<>();

	private List<Category> categories = new ArrayList<>();
//	@JsonIgnoreProperties("books")
//	private Set<Category> categories = new HashSet<>();

//	TODO: USE SETS INSTEAD OF LISTS (FIX INFINITE RECURSION WHEN USING SETS)


	public static BookStream stream(List<Book> books) {
		return new BookStream(books);
	}
}
