package com.vroomvroom.userservice.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SlackId {

	@Column(name = "slack_id", nullable = false)
	private String value;

	public SlackId(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Slack ID는 필수값입니다.");
		}
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SlackId slackId)) return false;
		return value.equals(slackId.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
