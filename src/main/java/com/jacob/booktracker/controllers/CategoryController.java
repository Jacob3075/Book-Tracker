package com.jacob.booktracker.controllers;

import com.jacob.booktracker.services.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class CategoryController {

	@Bean
	public RouterFunction<ServerResponse> categoryEndpointHandler(CategoryService categoryService) {
		return nest(
				path("api.book-store/categories/"),
				route(GET("/"), categoryService::findAll)
						.andRoute(POST("/"), categoryService::addNewCategory)
						.andRoute(PUT("/"), categoryService::updateCategory)
						.andRoute(DELETE("{id}"), categoryService::deleteById)
						.andRoute(GET("{id}"), categoryService::findById)
		);
	}

}
