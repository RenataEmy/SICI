package br.ifrn.edu.sici.controllers;

import br.ifrn.edu.sici.dtos.requests.CategoryRequestDTO;
import br.ifrn.edu.sici.dtos.responses.CategoryResponseDTO;
import br.ifrn.edu.sici.filters.CategoryFilter;
import br.ifrn.edu.sici.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/categorias")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @RequestBody @Valid CategoryRequestDTO data
    ) {
        CategoryResponseDTO response = categoryService.create(data);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.categoryId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> list(
            CategoryFilter filter,
            Pageable pageable) {
        return ResponseEntity.ok(categoryService.list(filter, pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> change(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO data
    ) {
        return ResponseEntity.ok(categoryService.change(id, data));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
