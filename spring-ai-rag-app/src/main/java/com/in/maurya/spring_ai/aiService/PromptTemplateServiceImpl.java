package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.aiServiceInterface.PromptTemplateService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PromptTemplateServiceImpl implements PromptTemplateService {

    public PromptTemplateServiceImpl(){}

    //create System and User prompt using PromptTemplateBuilder approach. This approach is recommended when working with medium or complex logic.
    //In this approach we need to create system/user template which are rendered to create prompts. This prompts can be sent to LLM.
    @Override
   public Prompt promptTemplateBuilder(String place, String days, String budget, boolean isTrekReq){
        //Create System Template
       var systemPromptTemplate = SystemPromptTemplate.builder().template("You are professional travel guide").build();
       //Create System Message
       var systemMessage  = systemPromptTemplate.createMessage();

        // Create Prompt Template
        PromptTemplate promptTemplate = PromptTemplate.builder().template("Identify the 'best-kept secrets' and top-rated landmarks in {PLACE}.\n" +
                "Design a {DAYS}-day itinerary that groups activities by location and trek/hike difficulties to minimize travel time.\n" +
                "Include and reserve days for famous local trek if traveller says {IS-TREK-REQ}\n" +
                "Include one local food recommendation and one tip for cultural etiquette for each day.\n" +
                "Ensure the plan fits a {BUDGET} budget.").build();

       //Render The Template to create rendered message
       var renderedMessage = promptTemplate.createMessage(Map.of(
               "PLACE",place,
               "DAYS",days,
               "IS-TREK-REQ", isTrekReq,
               "BUDGET", budget
       ));

       //Create Prompt
       return new Prompt(systemMessage,renderedMessage);
   }

}
