package com.in.maurya.spring_ai.help_desk_backend.repository;

import com.in.maurya.spring_ai.help_desk_backend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByEmail(String email);
}
