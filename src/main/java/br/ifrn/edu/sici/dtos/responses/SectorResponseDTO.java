package br.ifrn.edu.sici.dtos.responses;

import java.util.List;

public record SectorResponseDTO(
        Long sectorId,
        String name,
        String acronym,
        List<Long> categoriesId,
        List<Long> techsId
) {
}
