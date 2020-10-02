package com.jacob.booktracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	private String            id;
	private String            userId;
	private String            bookName;
	private String            description;
	private int               pages;
	private int               chapters;
	private int               lastReadChapter;
	private List<AuthorDTO>   authors;
	private List<CategoryDTO> categories;
}
