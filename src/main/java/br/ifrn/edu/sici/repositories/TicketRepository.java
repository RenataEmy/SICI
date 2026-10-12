package br.ifrn.edu.sici.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifrn.edu.sici.models.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findTicketByTicketId(Long ticketId);

    
}
