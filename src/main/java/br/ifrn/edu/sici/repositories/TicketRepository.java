package br.ifrn.edu.sici.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.ifrn.edu.sici.models.Ticket;

import java.util.Optional;

@Repository
public interface TicketRepository extends
        JpaRepository<Ticket, Long>,
        JpaSpecificationExecutor<Ticket>
{

    @EntityGraph(attributePaths = {"interactions"})
    Optional<Ticket> findByTicketId(Long ticketId);

}
