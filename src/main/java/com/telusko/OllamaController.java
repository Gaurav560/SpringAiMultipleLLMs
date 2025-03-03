package com.telusko;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ollama")
@CrossOrigin(origins = "*")
public class OllamaController {
    private final ChatClient chatClient;

    public OllamaController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/{message}")
    public ResponseEntity<String> promptWithPathVariable(@PathVariable String message) {
        try {
            String response = chatClient
                    .prompt(message)
                    .call()
                    .content();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
