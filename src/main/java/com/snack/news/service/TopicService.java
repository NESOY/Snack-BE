package com.snack.news.service;

import com.snack.news.domain.topic.Topic;
import com.snack.news.domain.topic.TopicSorting;
import com.snack.news.domain.topic.TopicType;
import com.snack.news.dto.TopicDto;
import com.snack.news.exception.TopicNotFoundException;
import com.snack.news.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class TopicService {
	private final TopicRepository topicRepository;

	@Transactional
	public Topic createTopic(TopicDto topicDto) {
		Topic topic = topicDto.getTopicNewEntity();

		try {
			topicRepository.save(topic);
		} catch (DataIntegrityViolationException e) {
			throw new TopicNotFoundException(); // 토픽 이름이 중복. todo: 예외 관리하기
		}

		return topic; // todo : Success Response
	}

	public List<Topic> getTypeTopicList(TopicType topicType, TopicSorting sorting) {
		List<Topic> topics = topicRepository.findAllByTypeIs(topicType);

		return topics.stream()
				.sorted(sorting.getOperator())
				.collect(toList());
	}


	public List<Topic> getTopicList(TopicSorting sorting) {
		List<Topic> topics = topicRepository.findAll();

		return topics.stream()
				.sorted(sorting.getOperator())
				.collect(toList());
	}

	public List<Topic> getTopicList(List<Long> topicIds) {
		if (Objects.isNull(topicIds)) {
			return Collections.emptyList();
		}

		return topicRepository.findByIdIn(topicIds);
	}


	@Transactional
	public Topic updateTopic(TopicDto topicDto) {
		Topic topic = topicDto.getTopicUpdateEntity();
		topicRepository.findById(topic.getId()).orElseThrow(TopicNotFoundException::new);

		return topicRepository.save(topic);
	}

	public Topic getTopic(TopicDto topicDto) {
		return topicRepository.findById(topicDto.getId()).orElseThrow(TopicNotFoundException::new);
	}
}
