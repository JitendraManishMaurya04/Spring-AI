package com.in.maurya.spring_ai.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

public class TokenConsumptionAdvisor  implements CallAdvisor, StreamAdvisor {

    private Logger logger = LoggerFactory.getLogger(TokenConsumptionAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        this.logger.info("TokenConsumptionAdvisor is Invoked");
        this.logger.info("TokenConsumptionAdvisor : Request - {}", chatClientRequest.prompt().getContents());

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        this.logger.info("TokenConsumptionAdvisor : Response received from the Model - {}", chatClientResponse
                                                                                                    .chatResponse()
                                                                                                    .getResult()
                                                                                                    .getOutput()
                                                                                                    .getText());
        this.logger.info("TokenConsumptionAdvisor : Prompt Token- {}", chatClientResponse
                                                                                                    .chatResponse()
                                                                                                    .getMetadata()
                                                                                                    .getUsage()
                                                                                                    .getPromptTokens());
        this.logger.info("TokenConsumptionAdvisor : Completion Token - {}", chatClientResponse
                                                                                                    .chatResponse()
                                                                                                    .getMetadata()
                                                                                                    .getUsage()
                                                                                                    .getCompletionTokens());
        this.logger.info("TokenConsumptionAdvisor : Total Token Consumed - {}", chatClientResponse
                                                                                                    .chatResponse()
                                                                                                    .getMetadata()
                                                                                                    .getUsage()
                                                                                                    .getTotalTokens());

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        this.logger.info("TokenConsumptionAdvisor is Invoked for Streaming Chat");
        Flux<ChatClientResponse> chatClientResponseFlux = streamAdvisorChain.nextStream(chatClientRequest);
        return chatClientResponseFlux;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
