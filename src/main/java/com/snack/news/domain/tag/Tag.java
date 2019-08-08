package com.snack.news.domain.tag;


import com.snack.news.domain.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@ToString
public class Tag extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Builder
	public Tag(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Tag tag = (Tag) o;

		if (!Objects.equals(id, tag.id)) return false;
		return Objects.equals(title, tag.title);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		return result;
	}
}