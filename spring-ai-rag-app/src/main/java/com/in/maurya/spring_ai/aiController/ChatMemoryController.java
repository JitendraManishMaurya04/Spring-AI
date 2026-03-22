package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.AdvanceRagServiceImpl;
import com.in.maurya.spring_ai.aiService.SimpleChatServiceImpl;
import com.in.maurya.spring_ai.aiService.VectorStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat-memory")
public class ChatMemoryController {

    private final SimpleChatServiceImpl promptServiceImpl;
    private final AdvanceRagServiceImpl advanceRagServiceImpl;

     public ChatMemoryController(SimpleChatServiceImpl promptServiceImpl, AdvanceRagServiceImpl advanceRagServiceImpl){
         this.promptServiceImpl = promptServiceImpl;
         this.advanceRagServiceImpl = advanceRagServiceImpl;
     }

     @GetMapping("/chat{persona}")
     public ResponseEntity<String> simpleChat(@RequestParam(value="chatInput", required=true) String chatInput,  @RequestHeader String userId){
         var response = promptServiceImpl.chatMemory(chatInput,userId);
         return ResponseEntity.ok(response);
     }

    @GetMapping("/vectordb/{advisorType}/chat")
    public ResponseEntity<String> travelPlannerVectorDbChat(@RequestParam(value="chatInput", required=true) String chatInput,  @PathVariable String advisorType){
        var response = promptServiceImpl.chatMemory(chatInput,advisorType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/advance-rag/chat")
    public ResponseEntity<String> travelPlannerAdvanceRagChat(@RequestParam(value="chatInput", required=true) String chatInput){
        var response = advanceRagServiceImpl.getResponse(chatInput);
        return ResponseEntity.ok(response);
    }

}
