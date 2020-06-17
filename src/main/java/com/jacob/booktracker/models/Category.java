package com.jacob.booktracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jacob.booktracker.utils.streams.CategoryStream;
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
@Table(name = "categories", schema = "book-store-dev")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_name")
	@NotEmpty
	private String categoryName;

	@ManyToMany(mappedBy = "categories")
	@JsonIgnore
	private List<Book> books = new ArrayList<>();
//	@JsonIgnoreProperties({"authors", "categories"})
//	private Set<Book> books = new HashSet<>();

	public static CategoryStream stream(List<Category> categories) {
		return new CategoryStream(categories);
	}
}
