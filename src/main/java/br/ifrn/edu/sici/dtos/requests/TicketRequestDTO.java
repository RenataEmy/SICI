package br.ifrn.edu.sici.dtos.requests;

import br.ifrn.edu.sici.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record TicketRequestDTO(
    @NotBlank(message = "O título é obrigatório") @Size(max = 100) String title,
    @NotBlank(message = "A descrição é obrigatória") @Size(max = 250) String description,
    @NotBlank(message = "O local é obrigatório") @Size(max = 50) String location,
    Priority priority,
    @NotNull(message = "A categoria é obrigatória") Long categoryId,
    @NotNull(message = "O solicitante é obrigatório") Long requestId
) {
    public TicketRequestDTO(String title, String description, String location, Priority priority, Long categoryId) {
        this(title, description, location, priority, categoryId, null);
    }
}
