package com.snack.news.controller;

import com.snack.news.domain.Topic;
import com.snack.news.domain.TopicType;
import com.snack.news.dto.TopicDto;
import com.snack.news.exception.TopicNotFoundException;
import com.snack.news.service.TopicService;
import com.snack.news.strategy.TopicSorting;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/topic")
public class TopicController {
	private final TopicService topicService;

	// todo : Domain이 직접 노출되는 문제
	@PostMapping
	public ResponseEntity<Topic> createTopic(@RequestBody TopicDto topicDto) {
		return ResponseEntity.of(Optional.of(topicService.createTopic(topicDto)));
	}

	@GetMapping
	public ResponseEntity<List<Topic>> getTopicList(@RequestParam(defaultValue = "NAME") TopicSorting sort) {
		return ResponseEntity.of(Optional.of(topicService.getTopicList(sort)));
	}

	@GetMapping("/{type}")
	public ResponseEntity<List<Topic>> getTopicList(@PathVariable TopicType type, @RequestParam(defaultValue = "NAME") TopicSorting sort) {
		return ResponseEntity.of(Optional.of(topicService.getTypeTopicList(type, sort)));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		// TODO: 에러 메시지 세분화
		return new ResponseEntity<>(new TopicNotFoundException().getMessage(), HttpStatus.BAD_REQUEST);
	}

	@PutMapping
	public ResponseEntity<Topic> updateTopic(@RequestBody TopicDto topicDto) {
		return ResponseEntity.of(Optional.of(topicService.updateTopic(topicDto)));
	}
}