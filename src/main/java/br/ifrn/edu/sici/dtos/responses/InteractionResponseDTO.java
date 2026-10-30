package br.ifrn.edu.sici.dtos.responses;

import java.time.LocalDateTime;

public record InteractionResponseDTO(
        Long interactionId,
        String text,
        LocalDateTime dateTime,
        Long authorId
) {
}
