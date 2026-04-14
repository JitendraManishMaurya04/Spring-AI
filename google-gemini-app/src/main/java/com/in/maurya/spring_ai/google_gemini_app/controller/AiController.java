package com.in.maurya.spring_ai.google_gemini_app.controller;

import com.in.maurya.spring_ai.google_gemini_app.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService){
        this.aiService = aiService;
    }

    @GetMapping("/google-gemini/chat")
    public ResponseEntity<String> googleGeminiAiChat(@RequestParam(value="query", required=true) String query){
        var response = aiService.getResponseFromAI(query);
        return ResponseEntity.ok(response);
    }

 }
