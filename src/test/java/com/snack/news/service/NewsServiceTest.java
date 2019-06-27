package com.snack.news.service;

import com.snack.news.domain.News;
import com.snack.news.dto.NewsDto;
import com.snack.news.exception.NewsNotFoundException;
import com.snack.news.fixture.NewsTestcase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceTest extends NewsTestcase {
	private static final String TEST_NEWS_TITLE = "test news title";
	private static final String TEST_NEWS_CONTENT = "test news content";
	
	@Autowired
	private NewsService newsService;

	@Test
	public void 뉴스를_생성할_수_있다() {
		int size = newsService.getNewsList().size();

		NewsDto newsDto = NewsDto.builder().title(TEST_NEWS_TITLE).content(TEST_NEWS_CONTENT).build();
		newsService.createNews(newsDto);

		assertThat(size + 1).isEqualTo(newsService.getNewsList().size());
	}

	@Test
	public void ID로_뉴스를_조회할_수_있다() {
		NewsDto newsDto = NewsDto.builder().title(TEST_TITLE).content(TEST_CONTENT).build();
		NewsDto savedNews = newsService.createNews(newsDto);
		Long id = savedNews.getId();

		News result = newsService.getNews(id);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getTitle()).isEqualTo(TEST_TITLE);
		assertThat(result.getContent()).isEqualTo(TEST_CONTENT);
	}

	@Test
	public void Topic_별로_뉴스를_조회할_수_있다() {
		final List<Long> testTopicIds = asList(1L, 2L);
		NewsDto newsDto = NewsDto.builder()
				.title(TEST_TITLE)
				.content(TEST_CONTENT)
				.topicIds(testTopicIds).build();

		List<Long> resultNewsIds = newsService.getTopicNewsList(newsDto)
				.stream()
				.map(News::getId)
				.collect(toList());

		List<Long> expectedResultNewsIds = newsService.getNewsList()
				.stream()
				.map(News::getId)
				.filter(testTopicIds::contains)
				.collect(toList());

		assertThat(resultNewsIds).containsAll(expectedResultNewsIds);
	}

	@Test(expected = NewsNotFoundException.class)
	public void ID가_유효하지않는다면_예외를_반환한다() {
		Long invalidNewsId = 999L;

		newsService.getNews(invalidNewsId);
	}
}