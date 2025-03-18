package com.telusko;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anthropic")
@CrossOrigin(origins = "*")
public class AnthropicController {

    private final ChatClient chatClient;

    public AnthropicController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/{message}")
    public ResponseEntity<String> promptWithPathVariable(@PathVariable String message) {
        try {
            var chatResponse = chatClient.prompt(message).call().chatResponse();
            if (chatResponse == null) {
                return ResponseEntity.badRequest().body("Error: Chat response was null");
            }

            String response = chatResponse.getResult().getOutput().getContent();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
