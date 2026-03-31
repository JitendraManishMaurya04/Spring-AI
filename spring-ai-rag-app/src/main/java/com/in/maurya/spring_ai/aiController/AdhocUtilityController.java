package com.in.maurya.spring_ai.aiController;

import com.in.maurya.spring_ai.aiService.DataLoaderImpl;
import com.in.maurya.spring_ai.aiService.VectorStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dataload")
public class AdhocUtilityController {

    private final VectorStoreService vectorStoreService;
    private final DataLoaderImpl dataLoaderImpl;


    public AdhocUtilityController(VectorStoreService vectorStoreService,  DataLoaderImpl dataLoaderImpl) {
        this.vectorStoreService = vectorStoreService;
        this.dataLoaderImpl = dataLoaderImpl;
    }

    @PostMapping ("/vectorstore/static-text-data")
    public ResponseEntity<String> textDataLoader(){
        var response = vectorStoreService.saveDataToVectorStoreDB();
        return ResponseEntity.ok(response);
    }

    @PostMapping ("/vectorstore/json-data")
    public ResponseEntity<String> jsonDataLoader(){
        var response = dataLoaderImpl.loadDocumentFromJson();
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping ("/vectorstore/pdf-data")
    public ResponseEntity<String> pdfDataLoader(){
        var response = dataLoaderImpl.loadDocumentFromPdf();
        return ResponseEntity.ok(response.toString());
    }

}
