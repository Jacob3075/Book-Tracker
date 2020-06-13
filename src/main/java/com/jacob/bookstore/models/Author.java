package com.jacob.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private List<Book> books;
}
