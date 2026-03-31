package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.ToolCallingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolCallerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ToolCallingService toolCallingService;

    public ToolCallerController( ToolCallingService toolCallingService){
        this.toolCallingService = toolCallingService;
    }


    @GetMapping("/chat") public ResponseEntity<String> toolCallingChat(@RequestParam(value="chatInput", required=true) String chatInput){
        this.logger.info("SimpleChatController: Tool calling API invoked!!!");
        var response = toolCallingService.toolCallingChat(chatInput);
        return ResponseEntity.ok(response);
    }
}
