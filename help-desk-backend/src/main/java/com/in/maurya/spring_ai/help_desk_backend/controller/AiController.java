package com.in.maurya.spring_ai.help_desk_backend.controller;

import com.in.maurya.spring_ai.help_desk_backend.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai/helpdesk")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat-response") public ResponseEntity<String> getResponse(@RequestBody String query, @RequestHeader("Conversation-Id") String conversationId){
        return ResponseEntity.ok(aiService.getResponseFromAssistant(query, conversationId));
    }

    @PostMapping("/streaming-response") public ResponseEntity<Flux<String>> getStreamingResponse(@RequestBody String query, @RequestHeader("Conversation-Id") String conversationId){
        var response = this.aiService.getStreamingResponseFromAssistant(query, conversationId);
        return ResponseEntity.ok(response);
    }
}
