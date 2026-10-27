package br.ifrn.edu.sici.dtos.requests;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SectorRequestDTO(
        @NotBlank(message = "O nome é obrigatório") @Size(max = 100) String name,
        @NotBlank(message = "A sigla é obrigatória") @Size(max = 6) String acronym,
        List<Long> categoriesId,
        List<Long> techsId,
        @NotNull(message = "O administrador é obrigatório") Long adminId
) {
}
