package com.in.maurya.spring_ai.config;

import com.in.maurya.spring_ai.advisor.TokenConsumptionAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
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

    // This client makes ue of ChatMemory to remember and use previous conversations
    // MessageWindowChatMemory object is used to  create a MessageChatMemoryAdvisor object. By Default only last 20 conversations are stored.
    //To Store the messages by default  MessageWindowChatMemory will use InMemoryCHatMemoryRepository which is one of the default implementation of ChatMemoryRepository.
    @Bean("openAiChatMemoryClient")
    ChatClient openAiChatMemoryClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        MessageChatMemoryAdvisor messageChatMemoryAdvisor =  MessageChatMemoryAdvisor.builder(chatMemory).build();
        return builder
                .defaultAdvisors(messageChatMemoryAdvisor, new SimpleLoggerAdvisor())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-nano")
                        .temperature(0.3)
                        .build()
                )
                .defaultSystem("You are a friendly chat bot that answers question in the voice of a {persona}")
                .build();
    }

    //Define custom bean for ChatMemory pass override default maxMessages property and JdbcChatMemoryRepository
    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(5)
                .build();
    }

}
