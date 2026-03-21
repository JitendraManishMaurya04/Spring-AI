package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.aiServiceInterface.PromptService;
import com.in.maurya.spring_ai.aienum.RagAdvisorType;
import com.in.maurya.spring_ai.dto.GenericResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    private final VectorStoreService vectorStoreService;
    private final VectorStore vectorStore;

    public SimpleChatServiceImpl(@Qualifier("openAiChatClient") ChatClient simplechatClient, @Qualifier("openAiChatClientTravelPlanner") ChatClient travelPlannerChatClient,
                                 @Qualifier("openAiChatMemoryClient") ChatClient chatMemoryClient, PromptTemplateServiceImpl promptTemplateServiceImpl, VectorStoreService vectorStoreService,
                                    VectorStore vectorStore){
        this.simpleChatClient = simplechatClient;
        this.travelPlannerChatClient = travelPlannerChatClient;
        this.chatMemoryClient = chatMemoryClient;
        this.promptTemplateServiceImpl = promptTemplateServiceImpl;
        this.vectorStoreService = vectorStoreService;
        this.vectorStore = vectorStore;
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

    //Simple-RAG flow implementation using manual, QNA/rag advisors
    @Override
    public String chatMemory(String prompt, String advisorType) {
        String result = null;
        //Manual approach of using vectorStore.similaritySearch method to fetch required data from vector DB and then passing to LLM
        if (advisorType.equals(RagAdvisorType.NO_ADVISOR.name())) {
            //search and load data from vector database
            String contextData = vectorStoreService.vectorSearch(prompt);

            //Invoke LLM
            result = chatMemoryClient.prompt(prompt)
                    .system(s -> s.text(systemMessage).param("documents", contextData))
                    .user(u -> u.text(userMessage).param("query", prompt))
                    .call()
                    .content();
        }
        //Using QuestionAndAnswerAdvisor provided by Spring AI to do similarity search and fetch required data from vector DB.
        else if (advisorType.equals(RagAdvisorType.QNA_ADVISOR.name())) {
            result =   chatMemoryClient.prompt(prompt)
                    .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                            .searchRequest(SearchRequest.builder()
                                    .similarityThreshold(0.6d)
                                    .topK(3)
                                    .build())
                            .build())
                    .user(u -> u.text(userMessage).param("query", prompt))
                    .call()
                    .content();
        }
        //Using RetrievalAugmentationAdvisor provided by Spring AI to do similarity search and fetch required data from vector DB by using "Naive RAG Advisor flow"
        else if (advisorType.equals(RagAdvisorType.RAG_ADVISOR.name())) {
            result =   chatMemoryClient.prompt(prompt)
                    .advisors(RetrievalAugmentationAdvisor.builder()
                            .documentRetriever(VectorStoreDocumentRetriever.builder()
                                    .vectorStore(this.vectorStore)
                                    .similarityThreshold(0.6d)
                                    .topK(3)
                                    .build())
                            .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                            .build())
                    .user(u -> u.text(userMessage).param("query", prompt))
                    .call()
                    .content();
        }
        return result;
    }

}