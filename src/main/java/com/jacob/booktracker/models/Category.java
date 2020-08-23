package com.jacob.booktracker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	@Id
	private String id;

	@NotEmpty
	private String categoryName;

	private List<Book> books = new ArrayList<>();
//	@JsonIgnoreProperties({"authors", "categories"})
//	private Set<Book> books = new HashSet<>();
}
