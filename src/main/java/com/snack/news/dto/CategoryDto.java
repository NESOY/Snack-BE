package com.snack.news.dto;

import com.snack.news.domain.Category;

public class CategoryDto {
	private Long id;
	private String title;

	public Category getNewEntity() {
		return Category.builder()
				.title(title)
				.build();
	}

	public Category getUpdateEntity() {
		return Category.builder()
				.id(id)
				.title(title)
				.build();
	}
}
