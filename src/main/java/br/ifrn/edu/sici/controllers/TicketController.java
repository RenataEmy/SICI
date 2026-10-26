package br.ifrn.edu.sici.controllers;

import br.ifrn.edu.sici.dtos.requests.InteractionRequestDTO;
import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.dtos.requests.TicketResolvedDTO;
import br.ifrn.edu.sici.dtos.responses.InteractionResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketDetailResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.filters.TicketFilter;
import br.ifrn.edu.sici.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/chamados")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(
            @RequestBody @Valid TicketRequestDTO requestDto,
            @RequestBody @Valid Long userId
    ) {
        TicketResponseDTO responseDto = ticketService.create(requestDto, userId);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.ticketId())
                .toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponseDTO>> list(
            TicketFilter filter,
            Pageable pageable) {

        Page<TicketResponseDTO> page = ticketService.listAll(filter, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDetailResponseDTO> getId(@PathVariable Long id) {
        TicketDetailResponseDTO responseDto = ticketService.get(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/assumir")
    public ResponseEntity<TicketResponseDTO> take(
            @PathVariable Long id,
            @RequestBody Long techId
    ){
        TicketResponseDTO responseDTO = ticketService.take(id, techId);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<TicketResponseDTO> resolve(
            @PathVariable Long id,
            @RequestBody @Valid TicketResolvedDTO requestDto) {

        TicketResponseDTO responseDto = ticketService.resolve(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancel(
            @PathVariable Long id,
            @RequestBody Long userId
    ) {
        String message = ticketService.cancel(id, userId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/{id}/interacoes")
    public ResponseEntity<InteractionResponseDTO> interaction(
            @PathVariable Long id,
            @RequestBody Long userId,
            @RequestBody @Valid InteractionRequestDTO requestDTO
            ){

        InteractionResponseDTO responseDTO = ticketService.interactions(id, userId, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    // falta o de listar com filtros

}
