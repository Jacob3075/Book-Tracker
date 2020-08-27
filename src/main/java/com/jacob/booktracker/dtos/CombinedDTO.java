package com.jacob.booktracker.dtos;

import com.jacob.booktracker.models.Author;
import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CombinedDTO {
	private Book   book;
	private List<Author> authors;
	private List<Category> categories;
}
