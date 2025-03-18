package com.telusko;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "*")
public class OpenAIController {

    private final ChatClient chatClient;

    public OpenAIController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    //This method extracts all levels of metadata from the AI response for analysis and logging.
    @GetMapping("/{message}")
    public ResponseEntity<String> promptWithPathVariable(@PathVariable String message) {
        try {
            // Step 1: Send the user's message to the AI model and get the complete response
            // This makes the API call to the AI service (e.g., OpenAI) and returns a structured response
            ChatResponse chatResponse = chatClient
                    .prompt(message)
                    .call()
                    .chatResponse();

            // Step 2: Check if we received a valid response
            if (chatResponse == null) {
                return ResponseEntity.badRequest().body("Error: Chat response was null");
            }

            // Step 3: Log the entire ChatResponse object
            // This includes all levels of metadata and the actual response content
            System.out.println("chatResponse::::" + chatResponse);

            // Step 4: Extract and log response-level metadata
            // This includes token counts, model info, and rate limit information
            String chatResponseMetadata = chatResponse.getMetadata().toString();
            System.out.println("Response Metadata: " + chatResponseMetadata);

            // Step 5: Get the list of generated responses
            // For most requests, this will contain a single response (generation)
            // If n>1 was specified in the options, this could contain multiple alternatives
            List<Generation> generations = chatResponse.getResults();
            System.out.println("Generations: " + generations);

            // Alternative for single generation (convenience method):
            // Generation generation = chatResponse.getResult();

            // Step 6: Extract the first generation from the list
            // Each generation represents a complete response alternative
            Generation firstGeneration = generations.get(0);
            System.out.println("First Generation: " + firstGeneration);

            // Step 7: Extract generation-level metadata
            // This includes information like why the generation stopped (finishReason)
            String generationMetadata = firstGeneration.getMetadata().toString();
            System.out.println("Generation Metadata: " + generationMetadata);

            // Step 8: Extract the AssistantMessage from the generation
            // The AssistantMessage contains the actual response text and message-level metadata
            AssistantMessage assistantMessage = firstGeneration.getOutput();
            Map<String, Object> messageMetadata = assistantMessage.getMetadata();
            System.out.println("Message Metadata: " + messageMetadata);

            // Step 9: Extract the final text content from the AssistantMessage
            // This is the actual text response from the AI model that we'll return to the client
            String response = assistantMessage.getContent();
            System.out.println("Response Content: " + response);

            // Step 10: Return the AI's response text to the client
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle any errors that occur during processing
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


}