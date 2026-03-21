package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.VectorStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dataload")
public class AdhocUtilityController {

    private final VectorStoreService vectorStoreService;


    public AdhocUtilityController(VectorStoreService vectorStoreService) {
        this.vectorStoreService = vectorStoreService;
    }

    @PostMapping ("/vectorstore")
    public ResponseEntity<String> simpleChat(){
        var response = vectorStoreService.saveDataToVectorStoreDB();
        return ResponseEntity.ok(response);
    }

}
