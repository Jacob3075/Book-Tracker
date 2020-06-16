package com.jacob.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jacob.bookstore.utils.streams.AuthorStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors", schema = "book-store-dev")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "author_name")
	@NotEmpty
	private String authorName;

	@ManyToMany(mappedBy = "authors")
	@JsonIgnore
	private List<Book> books = new ArrayList<>();
//	@JsonIgnoreProperties({"authors", "categories"})
//	private Set<Book> books = new HashSet<>();


	public static AuthorStream stream(List<Author> authors) {
//		return new AuthorStream(authors.stream());
		return new AuthorStream(authors);
	}
}
