package br.ifrn.edu.sici.mappers;

import br.ifrn.edu.sici.dtos.responses.CategoryResponseDTO;
import br.ifrn.edu.sici.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "sector.sectorId", target = "sectorId")
    CategoryResponseDTO toResponse(Category category);
}
