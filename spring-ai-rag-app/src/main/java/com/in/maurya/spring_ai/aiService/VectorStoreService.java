package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.aienum.RagAdvisorType;
import com.in.maurya.spring_ai.utility.VectorStoreContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VectorStore vectorStore;


    public VectorStoreService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String saveDataToVectorStoreDB(){
        List<String> dataList = VectorStoreContent.getData();
        List<Document> documentList = dataList.stream().map(Document::new).toList();
        this.vectorStore.add(documentList);
        return "data loaded to vector db";
    }


    public String vectorSearch(String prompt) {
        //search and load data from vector database by using similaritySearch method which accepts SearchRequest Object.
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(2)
                .similarityThreshold(0.6)
                .query(prompt)
                .build();
        List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
        List<String> docList = documents.stream().map(Document::getText).toList();
        String contextData = String.join(", ", docList);
        logger.info("VectorStoreService: Vector  DB context data {}", contextData);

        return contextData;
    }

}
