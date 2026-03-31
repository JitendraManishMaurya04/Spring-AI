package com.in.maurya.spring_ai.aiService;

import com.in.maurya.spring_ai.aiServiceInterface.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderImpl  implements DataLoader {

    private Logger logger = LoggerFactory.getLogger(DataLoaderImpl.class);

    @Value("classpath:Kasol Trek Details.json")
    private Resource jsonResource;
    @Value("classpath:Essential Trekking Rules.pdf")
    private Resource pdfResource;

    @Override
    public List<Document> loadDocumentFromJson() {
        logger.info("DataLoaderImpl: Invoked JSON data loader!!!");

        var jsonReader = new JsonReader(jsonResource,"trekking_destinations");
        var documentList = jsonReader.read();
        logger.info("DataLoaderImpl: JSON Document List Size is {}", documentList.size());
        documentList.forEach( item -> {
            logger.info(String.valueOf(item));
        });
        return documentList;
    }

    @Override
    public List<Document> loadDocumentFromPdf() {
        logger.info("DataLoaderImpl: Invoked PDF data loader!!!");

//        var pdfReader = new PagePdfDocumentReader(pdfResource, )
//        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/sample1.pdf",
//                PdfDocumentReaderConfig.builder()
//                        .withPageTopMargin(0)
//                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                                .withNumberOfTopTextLinesToDelete(0)
//                                .build())
//                        .withPagesPerDocument(1)
//                        .build());

        return List.of();
    }
}
