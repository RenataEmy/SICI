package br.ifrn.edu.sici.controllers;

import br.ifrn.edu.sici.dtos.requests.UserRequestDTO;
import br.ifrn.edu.sici.dtos.responses.UserResponseDTO;
import br.ifrn.edu.sici.filters.UserFilter;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;

    //GET (lista), GET /usuarios nome, tipo, setorId
    public UserController(
            UserService userService
    ){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO requestDTO){
        UserResponseDTO responseDTO = userService.create(requestDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.userId())
                .toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getId(@PathVariable Long id){
        UserResponseDTO responseDTO = userService.get(id);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}/chamados")
    public ResponseEntity<Page<TicketResponseDTO>> tickets(
            @PathVariable Long id,
            @RequestParam(required = false) Status status,
            Pageable pageable) {
        return ResponseEntity.ok(userService.tickets(id, status, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findWithFilters(
            UserFilter userFilter,
            Pageable pageable) {

        Page<UserResponseDTO> usersPage = userService.findWithFilters(userFilter, pageable);

        return ResponseEntity.ok(usersPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> change(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestDTO requestDTO
    ){
        UserResponseDTO responseDTO = userService.change(id, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }


}
