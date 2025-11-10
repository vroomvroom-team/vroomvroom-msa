package com.vroomvroom.slackservice.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.vroomvroom.slackservice.exception.SlackErrorCode;
import com.vroomvroom.slackservice.application.command.SendSlackMessageCommand;
import com.vroomvroom.slackservice.exception.SlackException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SlackWebClient {
	private final WebClient webClient = WebClient.create();

	public void sendSlackMessage(String webhookUrl, SendSlackMessageCommand command) {
		try {
			Mono<String> response = webClient.post()
											 .uri(webhookUrl)
											 .bodyValue(buildPayload(command))
											 .retrieve()
											 .bodyToMono(String.class);

			response.subscribe(
				success -> log.info("[SlackWebClient] Message sent successfully"),
				error -> {
					log.error("[SlackWebClient] Slack send failed", error);
					throw new SlackException(SlackErrorCode.SLACK_MESSAGE_SEND_FAILED);
				}
			);
		} catch (Exception e) {
			throw new SlackException(SlackErrorCode.SLACK_MESSAGE_SEND_FAILED);
		}
	}

	private String buildPayload(SendSlackMessageCommand command) {
		return """
        {
          "channel": "%s",
          "username": "%s",
          "text": "%s"
        }
        """.formatted(command.getChannel(), command.getUsername(), command.getText());
	}
}
