package br.ifrn.edu.sici.mappers;

import br.ifrn.edu.sici.dtos.requests.InteractionRequestDTO;
import br.ifrn.edu.sici.dtos.requests.TicketResolvedDTO;
import br.ifrn.edu.sici.dtos.responses.InteractionResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketDetailResponseDTO;
import br.ifrn.edu.sici.models.Interaction;
import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(source = "tech.userId", target = "techId")
    @Mapping(source = "request.userId", target = "requestId")
    @Mapping(source = "category.categoryId", target = "categoryId")
    TicketResponseDTO toResponseDTO(Ticket ticket);

    Ticket toEntity(TicketRequestDTO dto);

    @Mapping(source = "tech.userId", target = "techId")
    @Mapping(source = "request.userId", target = "requestId")
    @Mapping(source = "category.categoryId", target = "categoryId")
    TicketDetailResponseDTO toDetailDTO(Ticket ticket);

    TicketResolvedDTO toResolvedDTO(Ticket ticket);

    @Mapping(source = "ticketId", target = "ticket.ticketId")
    Interaction toEntityInteraction(InteractionRequestDTO interaction);

    @Mapping(source = "author.userId", target = "authorId")
    InteractionResponseDTO toInteractionDTO(Interaction interaction);

}
