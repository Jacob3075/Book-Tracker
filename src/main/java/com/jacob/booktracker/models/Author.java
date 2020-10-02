package com.jacob.booktracker.models;

import com.jacob.booktracker.utils.mono.AuthorMono;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "Authors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

	@Id
	private String id;

	@NotEmpty
	private String authorName;

	private Set<String> bookIds = new HashSet<>();

	public static AuthorMono mono(Mono<Author> author) {
		return new AuthorMono(author);
	}
}
