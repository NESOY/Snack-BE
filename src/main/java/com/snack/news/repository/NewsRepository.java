package com.snack.news.repository;

import com.snack.news.domain.news.News;
import com.snack.news.dto.NewsDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {
	// todo : 만약에 뉴스가 엄청 많아지게된다면? SQL Between 속도가 제대로 나올까? 다른 방법은 없을까?
	List<News> findByCreateAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

	@Override
	List<News> findByNewsDto(NewsDto newsDto);
}