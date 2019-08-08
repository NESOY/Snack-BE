package com.snack.news.domain.news;

import com.snack.news.domain.category.Category;
import com.snack.news.domain.common.BaseTimeEntity;
import com.snack.news.domain.tag.Tag;
import com.snack.news.domain.topic.Topic;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@Entity
public class News extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column
	private String link;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "news_topic",
			joinColumns = @JoinColumn(name = "news_id"),
			inverseJoinColumns = @JoinColumn(name = "topic_id"))
	private List<Topic> topics;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "news_tag",
			joinColumns = @JoinColumn(name = "news_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;


	@Builder
	public News(String title, String link, String content, Category category, List<Topic> topics, List<Tag> tags) {
		this.title = title;
		this.link = link;
		this.category = category;
		this.content = content;
		this.topics = topics;
		this.tags = tags;
	}
}