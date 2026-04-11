package com.in.maurya.spring_ai.help_desk_backend.tools;

import com.in.maurya.spring_ai.help_desk_backend.entity.Ticket;
import com.in.maurya.spring_ai.help_desk_backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class TicketDatabaseTool {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TicketService ticketService;

    @Tool(description = "This tool helps to create new ticket in database")
    public Ticket createTicketTool(@ToolParam(description = "Ticket fields required to create a new ticket") Ticket ticket){
        try {
            return ticketService.createTicket(ticket);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Tool(description = "This tool helps to get ticket details by email")
    public Ticket getTicketByEmailTool(@ToolParam(description = "User whose ticket details are required by passing email") String email){
        return ticketService.getTicketByEmail(email);
    }

    @Tool(description = "This tool helps to update existing ticket in database")
    public Ticket updateTicketTool(@ToolParam(description = "New Ticket details with ticket id") Ticket ticket){
        return ticketService.updateTicket(ticket);
    }

    @Tool(description = "This tool helps to get current system time")
    public LocalDateTime getCurrentTmeTool(){
        logger.info("TicketDatabaseTool: getCurrentTmeTool invoked to fetch system time");
        LocalDateTime ldt = Instant.ofEpochMilli(System.currentTimeMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        logger.info("TicketDatabaseTool: getCurrentTmeTool generated system time is : {}",ldt);
        return ldt;
    }

}
