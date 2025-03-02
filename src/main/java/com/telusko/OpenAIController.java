package com.telusko;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "*")
public class OpenAIController {

    private final ChatClient chatClient;

    public OpenAIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @GetMapping("/{message}")
    public ResponseEntity<String> promptWithPathVariable(@PathVariable String message) {
        try {
            String response = chatClient
                    .prompt(message)
                    .call()
                    .chatResponse()
                    .getResult()
                    .getOutput()
                    .getContent();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}