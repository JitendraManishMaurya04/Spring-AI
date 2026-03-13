package com.in.maurya.spring_ai.config;

import com.in.maurya.spring_ai.advisor.TokenConsumptionAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean("openAiChatClient")
    ChatClient openAiChatClient(ChatClient.Builder builder) {
        // You can add common advisors or default settings here
        return builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-nano")
                        .temperature(0.7)
                        .maxTokens(100)
                        .build()
                )
                .defaultSystem("You are a friendly chat bot that answers question in the voice of a {persona}")
                .build();
    }

    @Bean("openAiChatClientTravelPlanner")
    ChatClient openAiChatClientTravelPlanner(ChatClient.Builder builder) {
        // You can add common advisors or default settings here
        return builder
                .defaultAdvisors(new TokenConsumptionAdvisor(),  new SafeGuardAdvisor(List.of("bank", "credit card", "creditcard", "DOB", "phone number", "Password", "PIN")))
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-nano")
                        .temperature(0.8)
                        .maxTokens(300)
                        .build()
                )
                .build();
    }

}
