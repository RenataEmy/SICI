package br.ifrn.edu.sici.mappers;

import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketResponseDTO toResponseDTO(Ticket ticket);

    Ticket toEntity(TicketRequestDTO dto);
}
