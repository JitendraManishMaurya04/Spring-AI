package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.SimpleChatServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/travelplanner")
public class TravelPlannerController {

    private final SimpleChatServiceImpl promptServiceImpl;

     public TravelPlannerController(SimpleChatServiceImpl promptServiceImpl){
         this.promptServiceImpl = promptServiceImpl;
     }

     @GetMapping("/chat/v1/place/{place}/days/{days}/budget/{budget}/IsTrekKingRequired/{isTrekReq}")
     public ResponseEntity<String> simpleChat( @PathVariable String place, @PathVariable String days, @PathVariable String budget, @PathVariable boolean isTrekReq){
         var response = promptServiceImpl.travelPlannerChat(place,days,budget,isTrekReq);
         return ResponseEntity.ok(response);
     }

    @GetMapping("/chat/v2/place/{place}/days/{days}/budget/{budget}")
    public ResponseEntity<String> simpleChat2( @PathVariable String place, @PathVariable String days, @PathVariable String budget){
        var response = promptServiceImpl.travelPlannerChat(place,days,budget);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stream-chat/place/{place}/days/{days}/budget/{budget}")
    public ResponseEntity<Flux<String>> streamChat(@PathVariable String place, @PathVariable String days, @PathVariable String budget){
        var response = promptServiceImpl.travelPlannerStreamChat(place,days,budget);
        return ResponseEntity.ok(response);
    }


}
