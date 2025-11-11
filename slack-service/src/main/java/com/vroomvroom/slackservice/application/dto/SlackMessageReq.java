package com.vroomvroom.slackservice.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SlackMessageReq {

	private String channel;   // 슬랙 채널 이름
	private String username;  // 발신자 이름
	private String text;      // 메시지 내용

	public SlackMessageReq(String channel, String username, String text) {
		this.channel = channel;
		this.username = username;
		this.text = text;
	}
}
