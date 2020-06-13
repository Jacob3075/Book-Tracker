package com.jacob.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books", schema = "book-store-dev")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String name;
	@NotEmpty
	private String description;

	@ManyToMany
	@JoinTable(
			name = "book_author",
			joinColumns = @JoinColumn(columnDefinition = "books_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(columnDefinition = "authors_id", referencedColumnName = "id")
	)
	private List<Author> authors;

	@ManyToMany
	@JoinTable(
			name = "book_category",
			joinColumns = @JoinColumn(columnDefinition = "books_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(columnDefinition = "categories_id", referencedColumnName = "id")
	)
	private List<Category> categories;

//	TODO: USE SETS INSTEAD OF LISTS (FIX INFINITE RECURSION WHEN USING SETS)
}
