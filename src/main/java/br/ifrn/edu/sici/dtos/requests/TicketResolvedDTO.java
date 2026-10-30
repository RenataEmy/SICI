package br.ifrn.edu.sici.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TicketResolvedDTO(
        @NotNull(message = "O técnico é obrigatório") Long techId,
        @NotBlank(message = "A solução é obrigatória")
        @Size(max = 300) String solution
) {
}
