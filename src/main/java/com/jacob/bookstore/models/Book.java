package com.jacob.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

	private String name;
	private String description;

	@Column(name = "unit_price")
	private double unitPrice;
	@Column(name = "units_in_stock")
	private int    unitsInStock;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "book_author",
			joinColumns = @JoinColumn(columnDefinition = "books_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(columnDefinition = "authors_id", referencedColumnName = "id")
	)
	private List<Author> authors;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "book_category",
			joinColumns = @JoinColumn(columnDefinition = "books_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(columnDefinition = "categories_id", referencedColumnName = "id")
	)
	private List<Category> categories;
}
