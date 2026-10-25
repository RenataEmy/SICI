package br.ifrn.edu.sici.dtos.requests;

import java.util.List;

public record SectorRequestDTO(
        String name,
        String acronym,
        List<Long> categoriesId,
        List<Long> techsId
) {
}
