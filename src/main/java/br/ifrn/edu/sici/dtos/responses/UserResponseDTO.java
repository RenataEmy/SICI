package br.ifrn.edu.sici.dtos.responses;

import org.mapstruct.Mapping;

import java.util.List;

public record UserResponseDTO(
        Long userId,
        String email,
        int registration,
        String type,
        List<Long> ticketsId,
        Long sectorId
) {
}
