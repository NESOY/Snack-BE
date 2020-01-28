package com.snack.news.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestNewsDto {

	public static final int DEFAULT_LIMIT_NEWS_LIST_SIZE = 10;
	public static final int MIN_LIMIT_NEWS_LIST_SIZE = 1;
	public static final int MAX_LIMIT_NEWS_LIST_SIZE = 100;

	private Long lastNewsId = Long.MAX_VALUE;
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDateTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime endDateTime;

	private Long categoryId;
	private List<Long> topicIds;
	private List<Long> tagIds;

	@Min(MIN_LIMIT_NEWS_LIST_SIZE)
	@Max(MAX_LIMIT_NEWS_LIST_SIZE)
	private int limitSize = DEFAULT_LIMIT_NEWS_LIST_SIZE;
}
