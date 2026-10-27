package br.ifrn.edu.sici.mappers;

import br.ifrn.edu.sici.dtos.requests.SectorRequestDTO;
import br.ifrn.edu.sici.dtos.responses.SectorResponseDTO;
import br.ifrn.edu.sici.models.Sector;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorMapper {
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "techs", ignore = true)
    Sector toEntity(SectorRequestDTO dto);

    @Mapping(source = "categories", target = "categoriesId", qualifiedByName = "categoryIds")
    @Mapping(source = "techs", target = "techsId", qualifiedByName = "techIds")
    SectorResponseDTO toResponse(Sector sector);

    @Named("categoryIds")
    default List<Long> categoryIds(List<Category> categories) {
        return categories == null ? List.of() : categories.stream().map(Category::getCategoryId).toList();
    }

    @Named("techIds")
    default List<Long> techIds(List<User> users) {
        return users == null ? List.of() : users.stream().map(User::getUserId).toList();
    }
}
