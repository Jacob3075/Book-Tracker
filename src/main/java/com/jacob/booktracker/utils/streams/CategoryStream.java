package com.jacob.booktracker.utils.streams;

import com.jacob.booktracker.models.Book;
import com.jacob.booktracker.models.Category;
import com.jacob.booktracker.repositories.CategoryRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryStream implements ForwardingStream<Category> {
	private List<Category> categories;

	private final Supplier<Stream<Category>> streamSupplier = () -> this.categories.stream();

	public CategoryStream(List<Category> categories) {
		this.categories = categories;
	}

	public CategoryStream updateCategory(Long id, CategoryRepository repository) {
		Optional<Category> optionalOldCategory = repository.findById(id);
		Optional<Category> optionalNewCategory = this.getCategory();
		if (optionalNewCategory.isEmpty() || optionalOldCategory.isEmpty()) return new CategoryStream(List.of());

		Category newCategory = optionalNewCategory.get();
		Category oldCategory = optionalOldCategory.get();

		BeanUtils.copyProperties(newCategory, oldCategory, "id");
		return new CategoryStream(List.of(oldCategory));
	}

	private Optional<Category> getCategory() {
		return this.getStream()
		           .findFirst();
	}

	@Override
	public Stream<Category> getStream() {
		return this.streamSupplier.get();
	}

	public boolean saveCategory(CategoryRepository categoryRepository) {
		Optional<Category> optionalCategory = this.getCategory();
		if (optionalCategory.isEmpty()) return false;

		categoryRepository.saveAndFlush(optionalCategory.get());
		return true;
	}

	public CategoryStream getNewCategories(CategoryRepository categoryRepository) {
		return new CategoryStream(
				this.getStream()
				    .filter(category -> isNewCategory(category, categoryRepository))
				    .collect(Collectors.toList())
		);
	}

	private boolean isNewCategory(Category category, CategoryRepository repository) {
		return repository.findCategoryByCategoryName(category.getCategoryName()).isEmpty();
	}

	public void removeCategoriesFrom(Book book) {
		List<Category> categoriesToRemove = this.getStream().collect(Collectors.toList());
		book.getCategories().removeIf(categoriesToRemove::contains);
	}

	public void addCategoriesTo(Book book, CategoryRepository categoryRepository) {
		this.getStream()
		    .map(Category::getCategoryName)
		    .forEach(categoryName -> categoryRepository.findCategoryByCategoryName(categoryName)
		                                               .ifPresent(category -> book.getCategories().add(category))
		    );

	}
}
