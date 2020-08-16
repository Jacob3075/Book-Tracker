package com.jacob.booktracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jacob.booktracker.utils.streams.AuthorStream;
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
public class Author {

	@Id
	private String id;

	@NotEmpty
	private String authorName;

	@JsonIgnore
	private List<Book> books = new ArrayList<>();
//	@JsonIgnoreProperties({"authors", "categories"})
//	private Set<Book> books = new HashSet<>();


	public static AuthorStream stream(List<Author> authors) {
//		return new AuthorStream(authors.stream());
		return new AuthorStream(authors);
	}
}
