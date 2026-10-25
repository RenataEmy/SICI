package br.ifrn.edu.sici.mappers;

import br.ifrn.edu.sici.dtos.requests.SectorRequestDTO;
import br.ifrn.edu.sici.dtos.responses.SectorResponseDTO;
import br.ifrn.edu.sici.models.Sector;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectorMapper {
    Sector toEntity(SectorRequestDTO dto);

    SectorResponseDTO toResponse(Sector sector);
}
