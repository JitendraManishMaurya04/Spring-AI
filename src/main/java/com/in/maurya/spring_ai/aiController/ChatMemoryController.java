package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.SimpleChatServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ChatMemoryController {

    private final SimpleChatServiceImpl promptServiceImpl;

     public ChatMemoryController(SimpleChatServiceImpl promptServiceImpl){
         this.promptServiceImpl = promptServiceImpl;
     }

     @GetMapping("/chat-memory/{persona}")
     public ResponseEntity<String> simpleChat(@RequestParam(value="chatInput", required=true) String chatInput, @PathVariable String persona, @RequestHeader String userId){
         var response = promptServiceImpl.chatMemory(chatInput,persona,userId);
         return ResponseEntity.ok(response);
     }

}
