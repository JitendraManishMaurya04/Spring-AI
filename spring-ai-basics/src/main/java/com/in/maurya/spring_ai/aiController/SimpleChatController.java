package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.SimpleChatServiceImpl;
import com.in.maurya.spring_ai.dto.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SimpleChatController {

    private final SimpleChatServiceImpl promptServiceImpl;

     public SimpleChatController(SimpleChatServiceImpl promptServiceImpl){
         this.promptServiceImpl = promptServiceImpl;
     }

     @GetMapping("/chat/{persona}")
     public ResponseEntity<String> simpleChat(@RequestParam(value="chatInput", required=true) String chatInput, @PathVariable String persona){
         var response = promptServiceImpl.chat(chatInput,persona);
         return ResponseEntity.ok(response);
     }

    @GetMapping("/chat/metadata/{persona}")
    public ResponseEntity<String> getChatMetaData(@RequestParam(value="chatInput", required=true) String chatInput, @PathVariable String persona){
        var response = promptServiceImpl.chatMetaData(chatInput, persona);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/v2")
    public ResponseEntity<List<GenericResponse>> simpleChatWithPersona(@RequestParam(value="chatInput", required=true) String chatInput){
        var response = promptServiceImpl.chat(chatInput);
        return ResponseEntity.ok(response);
    }

}
