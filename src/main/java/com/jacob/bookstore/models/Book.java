package com.jacob.bookstore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long   id;
	private String title;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "book_author",
			joinColumns = @JoinColumn(columnDefinition = "books_id"),
			inverseJoinColumns = @JoinColumn(columnDefinition = "author_id")
	)
	private List<Author> authors;
}
