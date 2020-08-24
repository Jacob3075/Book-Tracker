package com.jacob.booktracker.controllers;

import com.jacob.booktracker.services.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class BookController {

	@Bean
	public RouterFunction<ServerResponse> bookEndpointsHandler(BookService bookService) {
		return nest(
				path("/api.book-store/books"),
				route(GET("/"), bookService::findAll)
						.andRoute(POST("/"), bookService::addNewBook)
						.andRoute(PUT("/"), bookService::updateBook)
						.andRoute(GET("/{id}"), bookService::findById)
						.andRoute(DELETE("/{id}"), bookService::deleteById)
		);
	}
}
