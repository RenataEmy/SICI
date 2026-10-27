package br.ifrn.edu.sici.mappers;


import br.ifrn.edu.sici.dtos.requests.UserRequestDTO;
import br.ifrn.edu.sici.dtos.responses.UserResponseDTO;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.models.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "sector", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    User toEntity(UserRequestDTO data);

    @Mapping(source = "sector.sectorId", target = "sectorId")
    @Mapping(source = "tickets", target = "ticketsId", qualifiedByName = "ticketIds")
    UserResponseDTO toResponse(User user);

    @Named("ticketIds")
    default List<Long> ticketIds(List<Ticket> tickets) {
        return tickets == null ? List.of() : tickets.stream().map(Ticket::getTicketId).toList();
    }
}
