package com.snack.news.controller;


import com.google.gson.Gson;
import com.snack.news.domain.TopicType;
import com.snack.news.dto.TopicDto;
import com.snack.news.fixture.TopicTestcase;
import com.snack.news.service.TopicService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TopicControllerTest extends TopicTestcase {

	@InjectMocks
	private TopicController topicController;

	private final static String TOPIC_API_URL = "/api/topic";

	@Mock
	private TopicService topicService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(topicController).build();
	}

	@Test
	public void 토픽_생성_요청이_정상적으로_이루어진다() throws Exception {
		TopicDto topicDtoWithNameAndType = TopicTestcase.TEST_TOPIC_DTO_FOR_CORRECT_REQUEST;
		String requestJsonBody = new Gson().toJson(topicDtoWithNameAndType);

		when(topicService.createTopic(any(TopicDto.class))).thenReturn(TopicTestcase.DUMMY);

		mockMvc.perform(post(TOPIC_API_URL)
				.contentType(MediaType.APPLICATION_JSON).content(requestJsonBody))
				.andExpect(status().isOk());
	}

	@Test
	public void 토픽_타입이_없다면_토픽_생성요청이_실패한다() throws Exception {
		final String randomTopicName = Long.toString(ThreadLocalRandom.current().nextLong());
		final TopicType testTopicType = TopicType.NONE;

		TopicDto requestTopicDto = TopicDto.builder().name(randomTopicName).type(testTopicType).build();
		String requestJsonBody = new Gson().toJson(requestTopicDto).replace(testTopicType.name(), "SOME_WRONG_TYPE");

		mockMvc.perform(post(TOPIC_API_URL)
				.contentType(MediaType.APPLICATION_JSON).content(requestJsonBody))
				.andExpect(status().is4xxClientError()); // todo: 자세한 오류 메시지 명시
	}

	@Test
	public void 원하는_타입의_토픽_리스트를_가져온다() throws Exception {
		mockMvc.perform(get(TOPIC_API_URL))
				.andExpect(status().isOk());
	}

	@Test
	public void 원하는_타입의_토픽_리스트를_ID순으로_가져온다() throws Exception {
		mockMvc.perform(get(TOPIC_API_URL + "?sort=ID"))
				.andExpect(status().isOk());
	}

	@Test
	public void 원하는_타입의_토픽_리스트를_이름순으로_가져온다() throws Exception {
		mockMvc.perform(get(TOPIC_API_URL + "?sort=NAME"))
				.andExpect(status().isOk());
	}
}