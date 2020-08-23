package com.jacob.booktracker.controllers;

import com.jacob.booktracker.services.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RestController
@RequestMapping(value = "/api.book-store/books/")
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
