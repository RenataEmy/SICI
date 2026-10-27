package br.ifrn.edu.sici.mappers;


import br.ifrn.edu.sici.dtos.requests.UserRequestDTO;
import br.ifrn.edu.sici.dtos.responses.UserResponseDTO;
import br.ifrn.edu.sici.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "type", target = "type")
    User toEntity(UserRequestDTO data);

    UserResponseDTO toResponse(User user);
}
