package com.in.maurya.spring_ai.google_gemini_app.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean("googleAiChatClient")
    ChatClient googleGeniiChatClient(ChatClient.Builder builder) {
        // You can add common advisors or default settings here
        return builder
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .model("gemini-2.5-flash-lite")
                        .temperature(0.7)
                        .maxOutputTokens(100)
                        .build()
                )
                .build();
    }

}
