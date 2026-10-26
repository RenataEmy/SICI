package br.ifrn.edu.sici.dtos.requests;

import java.util.List;

public record UserRequestDTO(
        String name,
        String email,
        int registration,
        String type,
        List<Long> ticketsId,
        Long sectorId
) {
}
