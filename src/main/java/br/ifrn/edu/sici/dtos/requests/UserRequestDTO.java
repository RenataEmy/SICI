package br.ifrn.edu.sici.dtos.requests;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100) String name,
        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") @Size(max = 100) String email,
        @Positive(message = "A matrícula deve ser positiva") int registration,
        @NotBlank(message = "O tipo de usuário é obrigatório") String type,
        List<Long> ticketsId,
        Long sectorId
) {
}
