package br.ifrn.edu.sici.DTOs;

public record TicketRequestDTO(
    String title,
    String description,
    String location,
    String priority,
    Long request_id,
    Long category_id
) {}
