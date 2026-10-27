package br.ifrn.edu.sici.controllers;

import br.ifrn.edu.sici.dtos.requests.SectorRequestDTO;
import br.ifrn.edu.sici.dtos.responses.CategoryResponseDTO;
import br.ifrn.edu.sici.dtos.responses.SectorResponseDTO;
import br.ifrn.edu.sici.mappers.CategoryMapper;
import br.ifrn.edu.sici.services.SectorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/setores")
public class SectorController {
    private final SectorService sectorService;
    private final CategoryMapper categoryMapper;

    public SectorController(
            SectorService sectorService,
            CategoryMapper categoryMapper
    ) {
        this.sectorService = sectorService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public ResponseEntity<SectorResponseDTO> create(
            @RequestBody @Valid SectorRequestDTO data
            ){
        SectorResponseDTO response = sectorService.create(data);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.sectorId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorResponseDTO> getId(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(sectorService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<SectorResponseDTO>> list(
            @RequestBody String name,
            Pageable pageable
    ){
        Page<SectorResponseDTO> page = sectorService.listAll(name, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/categorias")
    public ResponseEntity<List<CategoryResponseDTO>> categories(@PathVariable Long id) {
        return ResponseEntity.ok(sectorService.categories(id).stream().map(categoryMapper::toResponse).toList());
    }

}
