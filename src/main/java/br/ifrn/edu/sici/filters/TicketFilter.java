package br.ifrn.edu.sici.filters;

import static br.ifrn.edu.sici.specifications.TicketSpec.*;

import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.models.Ticket;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@Data
public class TicketFilter {
    private Status status;
    private Priority priority;
    private Long sectorId;
    private Long categoryId;
    private Long requestId;
    private Long techId;
    private LocalDateTime openDateStart;
    private LocalDateTime openDateEnd;

    public Specification<Ticket> toSpecification() {
        return statusEquals(status)
                .and(priorityEquals(priority))
                .and(sectorEquals(sectorId))
                .and(categoryEquals(categoryId))
                .and(requestEquals(requestId))
                .and(techEquals(techId))
                .and(openDateBetween(openDateStart, openDateEnd));
    }
}
