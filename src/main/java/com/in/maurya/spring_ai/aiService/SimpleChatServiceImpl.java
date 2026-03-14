package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.aiServiceInterface.PromptService;
import com.in.maurya.spring_ai.dto.GenericResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
public class SimpleChatServiceImpl implements PromptService {

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;
    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;


    private final ChatClient simpleChatClient;
    private final ChatClient travelPlannerChatClient;
    private final ChatClient chatMemoryClient;
    private final PromptTemplateServiceImpl promptTemplateServiceImpl;

    public SimpleChatServiceImpl(@Qualifier("openAiChatClient") ChatClient simplechatClient, @Qualifier("openAiChatClientTravelPlanner") ChatClient travelPlannerChatClient, @Qualifier("openAiChatMemoryClient") ChatClient chatMemoryClient, PromptTemplateServiceImpl promptTemplateServiceImpl){
        this.simpleChatClient = simplechatClient;
        this.travelPlannerChatClient = travelPlannerChatClient;
        this.chatMemoryClient = chatMemoryClient;
        this.promptTemplateServiceImpl = promptTemplateServiceImpl;
    }

    @Override
    public String chat(String prompt, String persona) {
        return  simpleChatClient.prompt(prompt)
                .system(s -> s.param("persona", persona))
                .call()
                .content();
    }

    @Override
    public String chatMetaData(String prompt, String persona) {
        return  simpleChatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getMetadata()
                .toString();
    }

    @Override
    public List<GenericResponse> chat(String prompt) {
        return  simpleChatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<GenericResponse>>() {
                });
    }

    public String travelPlannerChatProxy(String place, String days, String budget, boolean isTrekReq, String apiVer){
        if (apiVer.equals("v1")){
            return travelPlannerChat(place, days, budget,isTrekReq);
        }
        return travelPlannerChat(place, days, budget);
    }

    @Override
    //This method makes use of Prompt Templating - Used for medium/complex scenario's
    public String travelPlannerChat(String place, String days, String budget, boolean isTrekReq) {
        var prompt = this.promptTemplateServiceImpl.promptTemplateBuilder(place,  days, budget, isTrekReq);
        return this.travelPlannerChatClient.prompt(prompt).call().content();
    }
    @Override
    //This method makes use of  chatClient Fluent API - Used for Simple /Medium scenario's
     public String travelPlannerChat(String place, String days, String budget) {
        Map<String,Object> usrParamMap = Map.of(   "PLACE",place,
                "DAYS",days,
                "BUDGET", budget);
        return this.travelPlannerChatClient.prompt( )
                .system(s -> s.text(systemMessage).param("persona", "travel guide"))
                .user(u -> u.text(userMessage).params(usrParamMap))
                .call()
                .content();
    }


    public Flux<String> travelPlannerStreamChat(String place, String days, String budget) {
        Map<String,Object> usrParamMap = Map.of(   "PLACE",place,
                "DAYS",days,
                "BUDGET", budget);
        return this.travelPlannerChatClient.prompt( )
                .system(s -> s.text(systemMessage).param("persona", "travel guide"))
                .user(u -> u.text(userMessage).params(usrParamMap))
                .stream()
                .content();
    }

    public String chatMemory(String prompt, String persona, String userId) {
        return  chatMemoryClient.prompt(prompt)
                .advisors( adv -> adv.param(ChatMemory.CONVERSATION_ID, userId))
                .system(s -> s.param("persona", persona))
                .call()
                .content();
    }
}