package com.vroomvroom.slackservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessage {
	private String username;
	private String text;
	private String icon_emoji;
}
