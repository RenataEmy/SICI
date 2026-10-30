package br.ifrn.edu.sici.dtos.responses;

import java.util.List;

public record UserResponseDTO(
        Long userId,
        String name,
        String email,
        int registration,
        String type,
        List<Long> ticketsId,
        Long sectorId
) {
}
