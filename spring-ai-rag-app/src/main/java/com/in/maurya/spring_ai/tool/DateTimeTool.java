package com.in.maurya.spring_ai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTool {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    //Information retrieval tool
    @Tool(description = "Get current date & time os users zone.")
    public String getDateTimeTime() {
        this.logger.info("DateTimeTool: Tool call invoked!!!");
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }

    //Action tool
    @Tool(description = "Set alarm for given time")
    public String SetAlarm(@ToolParam(description = "Time in ISO-8601 format") String time) {
        var dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        this.logger.info("DateTimeTool: Alarm Tool call invoked - {}", dateTime);
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }
}
