package br.ifrn.edu.sici.dtos.responses;

import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public record TicketDetailResponseDTO(
        Long ticketId,
        String title,
        String description,
        String location,
        Priority priority,
        Status status,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        String solution,
        Long requestId,
        Long categoryId,
        Long techId,
        List<InteractionResponseDTO> interactions
        ){
}
