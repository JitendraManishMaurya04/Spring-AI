package com.in.maurya.spring_ai.google_gemini_app.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient googleAiChatClient;

    public AiService(@Qualifier("googleAiChatClient") ChatClient googleAiChatClient){
        this.googleAiChatClient = googleAiChatClient;
    }

    public String getResponseFromAI(String prompt){
        return googleAiChatClient.prompt(prompt)
                .call()
                .content();
    }
}
