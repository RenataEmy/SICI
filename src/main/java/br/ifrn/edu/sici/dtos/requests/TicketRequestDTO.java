package br.ifrn.edu.sici.dtos.requests;

import br.ifrn.edu.sici.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record TicketRequestDTO(
    String title,
    String description,
    String location,
    Priority priority,
    @NotNull(message = "A categoria é obrigatória")
    Long categoryId
) {}
