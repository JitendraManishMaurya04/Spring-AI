package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.aiServiceInterface.AdvanceRagService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.join.DocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdvanceRagServiceImpl implements AdvanceRagService {

    private final ChatClient openAiChatClientAdvRag;
    private final VectorStore vectorStore;

    public AdvanceRagServiceImpl( @Qualifier("openAiChatClientAdvRag") ChatClient openAiChatClientAdvRag, VectorStore vectorStore){
        this.openAiChatClientAdvRag = openAiChatClientAdvRag;
        this.vectorStore = vectorStore;
    }

    @Override
    public String getResponse(String userQuery) {

        var advisor = RetrievalAugmentationAdvisor.builder()
                //Pre-processing stage
                .queryTransformers(
                        RewriteQueryTransformer.builder()
                                 .chatClientBuilder(openAiChatClientAdvRag.mutate().clone())
                                 .build(),
                        TranslationQueryTransformer.builder()
                                 .chatClientBuilder(openAiChatClientAdvRag.mutate().clone())
                                 .targetLanguage("english")
                                 .build()
                )
                .queryExpander(MultiQueryExpander.builder()
                                .chatClientBuilder(openAiChatClientAdvRag.mutate().clone())
                                .numberOfQueries(3)
                                .build())
                //Retrieval stage
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(this.vectorStore)
                        .similarityThreshold(0.3d)
                        .topK(3)
                        .build())
                .documentJoiner(new ConcatenationDocumentJoiner())
                .queryAugmenter(ContextualQueryAugmenter.builder().build())
                .build();

        return this.openAiChatClientAdvRag.prompt(userQuery)
                .advisors(advisor)
                .call()
                .content();
    }
}