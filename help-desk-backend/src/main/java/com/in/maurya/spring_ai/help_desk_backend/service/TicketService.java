package com.in.maurya.spring_ai.help_desk_backend.service;

import com.in.maurya.spring_ai.help_desk_backend.entity.Ticket;
import com.in.maurya.spring_ai.help_desk_backend.repository.TicketRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    //Create ticket
    @Transactional
    public Ticket createTicket(Ticket ticket){
        ticket.setId(null);
        return ticketRepository.save(ticket);
    }

    //Update ticket
    public Ticket updateTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    //Get ticket details by ticketId
    public Ticket getTicket(Long ticketId){
        return ticketRepository.findById(ticketId).orElse(null);
    }

    //Get ticket details by email
    public Ticket getTicketByEmail(String  email){
        return ticketRepository.findByEmail(email).orElse(null);
    }

}
