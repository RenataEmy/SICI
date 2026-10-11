package br.ifrn.edu.sici.dtos.responses;

import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.User;

import java.time.LocalDateTime;
import java.util.Optional;

public record TicketResponseDTO(
        Long ticketId,
        String title,
        String description,
        String location,
        Priority priority,
        Status status,
        LocalDateTime openDate,
        Optional<LocalDateTime> closeDate,
        String solution,
        Category categoryId,
        Optional<User> tech

    ) {
}
