package br.ifrn.edu.sici.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100) String name,
        @NotBlank(message = "A descrição é obrigatória") @Size(max = 200) String description,
        @NotNull(message = "O setor é obrigatório") Long sectorId,
        @NotNull(message = "O administrador é obrigatório") Long adminId
) {}
