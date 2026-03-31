package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.tool.DateTimeTool;
import com.in.maurya.spring_ai.tool.WeatherApiTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ToolCallingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatClient simpleChatClient;
    private final WeatherApiTool weatherApiTool;

    public ToolCallingService(@Qualifier("openAiChatClientTravelPlanner") ChatClient simplechatClient, WeatherApiTool weatherApiTool){
        this.simpleChatClient = simplechatClient;
        this.weatherApiTool = weatherApiTool;
    }

    public String toolCallingChat(String prompt){
        this.logger.info("ToolCallingService: Tool calling chat invoked!!!");
        return simpleChatClient.prompt()
                .tools(new DateTimeTool(),weatherApiTool)
                .user(prompt)
                .call()
                .content();
    }

}
