package com.snack.news.service;

import com.snack.news.domain.category.Category;
import com.snack.news.domain.news.News;
import com.snack.news.domain.tag.Tag;
import com.snack.news.domain.topic.Topic;
import com.snack.news.dto.NewsDto;
import com.snack.news.exception.CategoryNotFoundException;
import com.snack.news.exception.NewsNotFoundException;
import com.snack.news.fixture.NewsTestcase;
import com.snack.news.repository.NewsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
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

	@Autowired
	private NewsRepository newsRepository;

	@Test
	@Transactional
	public void 뉴스를_생성할_수_있다() {
		int size = newsService.getAllNewsList().size();

		NewsDto newsDto = NewsDto.builder().title(TEST_NEWS_TITLE).content(TEST_NEWS_CONTENT).categoryId(1L).build();
		newsService.createNews(newsDto);

		assertThat(size + 1).isEqualTo(newsService.getAllNewsList().size());
	}

	@Test(expected = CategoryNotFoundException.class)
	@Transactional
	public void 뉴스를_생성할_때_카테고리가_주어지지_않는다면_예외를_반환한다() {
		int size = newsService.getAllNewsList().size();

		NewsDto newsDto = NewsDto.builder().title(TEST_NEWS_TITLE).content(TEST_NEWS_CONTENT).build();
		newsService.createNews(newsDto);

		assertThat(size + 1).isEqualTo(newsService.getAllNewsList().size());
	}

	@Test
	@Transactional
	public void ID로_뉴스를_조회할_수_있다() {
		NewsDto newsDto = NewsDto.builder().title(TEST_TITLE).content(TEST_CONTENT).categoryId(1L).build();
		NewsDto savedNews = newsService.createNews(newsDto);
		Long id = savedNews.getId();

		News result = newsService.getNews(id);

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getTitle()).isEqualTo(TEST_TITLE);
		assertThat(result.getContent()).isEqualTo(TEST_CONTENT);
	}

	@Test(expected = NewsNotFoundException.class)
	@Transactional
	public void ID가_유효하지않는다면_예외를_반환한다() {
		Long invalidNewsId = 999L;

		newsService.getNews(invalidNewsId);
	}

	@Test
	@Transactional
	public void Topic_별로_뉴스를_조회할_수_있다() {
		final List<Long> testTopicIds = asList(2L, 3L);
		NewsDto newsDto = NewsDto.builder()
				.title(TEST_TITLE)
				.content(TEST_CONTENT)
				.topicIds(testTopicIds).build();

		List<Long> resultNewsIds = newsService.getNewsList(newsDto)
				.stream()
				.map(News::getId)
				.collect(toList());

		List<Long> expectedResultNewsIds = newsService.getAllNewsList()
				.stream()
				.filter(topics -> topics.getTopics()
						.stream()
						.map(Topic::getId)
						.anyMatch(testTopicIds::contains))
				.map(News::getId)
				.collect(toList());

		assertThat(resultNewsIds).containsOnlyElementsOf(expectedResultNewsIds);
	}

	@Test
	@Transactional
	public void 원하는_기간의_뉴스들을_조회할_수_있다() {

		long totalNewsCount = newsRepository.count();
		NewsDto newsDtoBeforeJune = NewsDto.builder()
				.startDateTime(LocalDateTime.of(2019, 1, 1, 0, 0))
				.endDateTime(LocalDateTime.of(2019, 6, 30, 0, 0))
				.build();

		long newsListCountBeforeJune = newsService.getNewsList(newsDtoBeforeJune).size();

		NewsDto newsDtoAfterJune = NewsDto.builder()
				.startDateTime(LocalDateTime.of(2019, 7, 1, 0, 0))
				.endDateTime(LocalDateTime.of(2019, 12, 31, 0, 0))
				.build();

		long newsListCountAfterJune = newsService.getNewsList(newsDtoAfterJune).size();

		assertThat(newsListCountBeforeJune + newsListCountAfterJune).isEqualTo(totalNewsCount);
	}

	@Test
	@Transactional
	public void Tag_별로_뉴스를_조회할_수_있다() {
		List<Long> tagIds = Collections.singletonList(1L);
		NewsDto newsDto = NewsDto.builder()
				.tagIds(tagIds)
				.build();

		List<Long> resultNewsIds = newsService.getNewsList(newsDto)
				.stream()
				.map(News::getId)
				.collect(toList());

		List<Long> expectedResultNewsIds = newsService.getAllNewsList()
				.stream()
				.filter(news -> news.getTags()
						.stream()
						.map(Tag::getId)
						.anyMatch(tagIds::contains))
				.map(News::getId)
				.collect(toList());

		assertThat(resultNewsIds).containsOnlyElementsOf(expectedResultNewsIds);
	}


	@Test
	@Transactional
	public void 원하는_Category의_뉴스를_조회할_수_있다() {

		Category category = Category.builder().id(2L).title("커머스").build();
		NewsDto newsDto = NewsDto.builder()
				.categoryId(category.getId())
				.build();

		List<Long> resultNewsIds = newsService.getNewsList(newsDto)
				.stream()
				.map(News::getId)
				.collect(toList());

		List<Long> expectedResultNewsIds = newsService.getAllNewsList()
				.stream()
				.filter(n -> n.getCategory().equals(category))
				.map(News::getId)
				.collect(toList());

		assertThat(resultNewsIds).containsOnlyElementsOf(expectedResultNewsIds);
	}

	@Test
	@Transactional
	public void 중복_조건에_해당하는_뉴스를_조회할_수_있다() {
		final List<Long> testTopicIds = asList(1L, 2L);
		final LocalDateTime start = LocalDateTime.of(2019, 7, 1, 0, 0);
		final LocalDateTime end = LocalDateTime.of(2019, 8, 31, 0, 0);
		final Category category = Category.builder().id(2L).title("커머스").build();
		final List<Long> tagIds = Collections.singletonList(1L);

		NewsDto newsDto = NewsDto.builder()
				.startDateTime(start)
				.endDateTime(end)
				.categoryId(category.getId())
				.topicIds(testTopicIds)
				.tagIds(tagIds)
				.build();

		List<Long> actualResultNewsIds = newsService.getNewsList(newsDto)
				.stream()
				.map(News::getId)
				.collect(toList());

		List<Long> expectedResultNewsIds = newsService.getAllNewsList()
				.stream()
				.filter(n -> n.getCreateAt().isBefore(end))
				.filter(n -> n.getCreateAt().isAfter(start))
				.filter(n -> n.getCategory().equals(category))
				.filter(topics -> topics.getTopics()
						.stream()
						.map(Topic::getId)
						.anyMatch(testTopicIds::contains))
				.filter(news -> news.getTags()
						.stream()
						.map(Tag::getId)
						.anyMatch(tagIds::contains))
				.map(News::getId)
				.collect(toList());

		assertThat(actualResultNewsIds).containsOnlyElementsOf(expectedResultNewsIds);
	}
}