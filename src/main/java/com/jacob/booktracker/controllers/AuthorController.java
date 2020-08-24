package com.jacob.booktracker.controllers;

import com.jacob.booktracker.services.AuthorService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class AuthorController {

	@Bean
	public RouterFunction<ServerResponse> authorEndpoints(AuthorService authorService) {
		return nest(
				path("/api.book-store/authors"),
				route(GET("/"), authorService::findAll)
						.andRoute(POST("/"), authorService::addNewAuthor)
						.andRoute(PUT("/"), authorService::updateAuthor)
						.andRoute(GET("{id}"), authorService::findById)
						.andRoute(DELETE("{id}"), authorService::deleteById)
		);
	}

}
