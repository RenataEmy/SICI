package br.ifrn.edu.sici.dtos.requests;

import br.ifrn.edu.sici.enums.Status;

import java.util.Optional;

public record TicketRequestDTO(
    String title,
    String description,
    String location,
    Optional<Status> priority,
    Optional<Long> techId,
    Long categoryId
) {}
