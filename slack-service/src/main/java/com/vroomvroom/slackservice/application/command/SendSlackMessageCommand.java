package com.vroomvroom.slackservice.application.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendSlackMessageCommand {
	private String channel;
	private String username;
	private String text;

	public SendSlackMessageCommand(String channel, String username, String text) {
		this.channel = channel;
		this.username = username;
		this.text = text;
	}
}
