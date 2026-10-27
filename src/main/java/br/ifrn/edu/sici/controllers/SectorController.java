package br.ifrn.edu.sici.controllers;

import br.ifrn.edu.sici.dtos.responses.CategoryResponseDTO;
import br.ifrn.edu.sici.mappers.CategoryMapper;
import br.ifrn.edu.sici.services.SectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}/categorias")
    public ResponseEntity<List<CategoryResponseDTO>> categories(@PathVariable Long id) {
        return ResponseEntity.ok(sectorService.categories(id).stream().map(categoryMapper::toResponse).toList());
    }
}
