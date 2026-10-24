package br.ifrn.edu.sici.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record TicketResolvedDTO(
        Long techId,
        @NotNull(message = "A solução é obrigatória")
        String solution
) {
}
