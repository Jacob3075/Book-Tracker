package com.jacob.booktracker.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	private Long              id;
	private String            name;
	private List<AuthorDTO>   authors;
	private List<CategoryDTO> categories;
	private int               pages;
	private int               chapters;
	private int               lastReadChapter;
}
