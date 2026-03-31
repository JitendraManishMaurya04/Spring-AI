package com.in.maurya.spring_ai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class WeatherApiTool {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${tool.weather.api-key}")
    private String weatherApiKey;
    private final RestClient restClient;

    public WeatherApiTool(RestClient restClient){
        this.restClient = restClient;
    }

    @Tool(description = "Get weather information of given city or place")
    public String getWeatherInformationByCity(@ToolParam(description = "city or place of which we want to get weather information") String city) {
        this.logger.info("WeatherApiTool: Weather information retrieval tool call invoked for city/place - {}", city);
        var response = restClient
                .get()
                .uri(builder -> builder.path("/current.json")
                        .queryParam("key", weatherApiKey)
                        .queryParam("q",city)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String,Object>>() {
                });
        this.logger.info("WeatherApiTool: API Response- {}", response.toString());
        return response.toString();
    }
}
