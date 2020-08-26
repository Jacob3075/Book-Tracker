package com.jacob.booktracker.utils;

import com.jacob.booktracker.repositories.AuthorRepository;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StaticContextInitializer {

	private final AuthorRepository authorRepository;

	private final CategoryRepository categoryRepository;

	public StaticContextInitializer(
			AuthorRepository authorRepository,
			CategoryRepository categoryRepository) {
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
	}

	@PostConstruct
	private void initialize() {
		CommonUtils.setAuthorRepository(authorRepository);
		CommonUtils.setCategoryRepository(categoryRepository);
	}

}
