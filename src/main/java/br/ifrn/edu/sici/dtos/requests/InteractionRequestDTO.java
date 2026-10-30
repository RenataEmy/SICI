package br.ifrn.edu.sici.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InteractionRequestDTO(
        @NotBlank(message = "O texto da interação é obrigatório") @Size(max = 300) String text,
        @NotNull(message = "O autor é obrigatório") Long userId
) {
}
