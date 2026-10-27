package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.SectorRequestDTO;
import br.ifrn.edu.sici.dtos.responses.SectorResponseDTO;
import br.ifrn.edu.sici.mappers.SectorMapper;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.Sector;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.SectorRepository;
import br.ifrn.edu.sici.repositories.UserRepository;
import br.ifrn.edu.sici.repositories.TicketRepository;
import br.ifrn.edu.sici.repositories.CategoryRepository;
import br.ifrn.edu.sici.exceptions.DatabaseException;
import br.ifrn.edu.sici.exceptions.ResourceNotFoundException;
import br.ifrn.edu.sici.exceptions.RuleConflit;
import br.ifrn.edu.sici.specifications.SectorSpec;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;
    private final UserRepository userRepository;
    private final SectorMapper sectorMapper;
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;

    public SectorService(
            SectorRepository sectorRepository,
            UserRepository userRepository,
            SectorMapper sectorMapper,
            TicketRepository ticketRepository,
            CategoryRepository categoryRepository
    ){
        this.sectorRepository = sectorRepository;
        this.userRepository = userRepository;
        this.sectorMapper = sectorMapper;
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
    }

    public SectorResponseDTO create(SectorRequestDTO data, Long adminId){
        User admin = userRepository.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (admin.getType() != br.ifrn.edu.sici.enums.UserType.ADMINISTRADOR){
            throw new RuleConflit("Apenas usuário do tipo administrador pode cadastrar um setor");
        }

        Optional<Sector> entity = sectorRepository.findByAcronym(data.acronym());

        if(entity.isPresent()){
            throw new RuleConflit("Já existe setor cadastrado com essa sigla");
        }

        Sector newSector = sectorMapper.toEntity(data);

        if (data.categoriesId() != null) {
            List<Category> categories = categoryRepository.findAllById(data.categoriesId());
            if (categories.size() != data.categoriesId().size()) throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas");
            categories.forEach(category -> category.setSector(newSector));
            newSector.setCategories(new java.util.ArrayList<>(categories));
        }
        if (data.techsId() != null) {
            List<User> techs = userRepository.findAllById(data.techsId());
            if (techs.size() != data.techsId().size() || techs.stream().anyMatch(tech -> tech.getType() != br.ifrn.edu.sici.enums.UserType.TECNICO)) {
                throw new ResourceNotFoundException("Um ou mais técnicos não foram encontrados");
            }
            techs.forEach(tech -> tech.setSector(newSector));
            newSector.setTechs(new java.util.ArrayList<>(techs));
        }

        newSector = sectorRepository.save(newSector);

        return sectorMapper.toResponse(newSector);
    }

    public Sector findId(Long sectorId){
         Sector sector = sectorRepository.findById(sectorId)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));
         return sector;
    }

    public SectorResponseDTO get(Long sectorId){
        Sector sector = sectorRepository.findById(sectorId)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi localizado nenhum setor com essa identificação"));

        return sectorMapper.toResponse(sector);
    }

    public Page<SectorResponseDTO> listAll(String name, Pageable pageable){
        Specification<Sector> spec = SectorSpec.nameContains(name);
        Page<Sector> sectorsPage = sectorRepository.findAll(spec, pageable);

        if(sectorsPage.isEmpty()){
            throw new ResourceNotFoundException("Nenhum setor localizado");
        }

        return sectorsPage.map(sectorMapper::toResponse);
    }

    @Transactional
    public SectorResponseDTO change(Long id, Long adminId, SectorRequestDTO data) {
        // 1. Busca o setor existente
        Sector sector = sectorRepository .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));

        // 2. Atualiza os dados básicos
        sector.setName(data.name());
        sector.setAcronym(data.acronym());

        // 3. Atualiza as Categorias (@OneToMany)
        if (data.categoriesId() != null) {
            // Busca as categorias no banco pelos IDs recebidos
            List<Category> categories = categoryRepository.findAllById(data.categoriesId());
            if (categories.size() != data.categoriesId().size()){
                throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas");
            }

            if (sector.getCategories() != null) {
                sector.getCategories().stream().filter(
                        category -> !categories.contains(category))
                        .forEach(category -> category.setSector(null));
            }

            Sector finalSector = sector;
            categories.forEach(category -> category.setSector(finalSector));

            sector.setCategories(new java.util.ArrayList<>(categories));
        }

        if (data.techsId() != null) {
            List<User> techs = userRepository.findAllById(data.techsId());
            if (techs.size() != data.techsId().size() || techs.stream().anyMatch(
                    tech -> tech.getType() != br.ifrn.edu.sici.enums.UserType.TECNICO)
            ) {
                throw new ResourceNotFoundException("Um ou mais técnicos não foram encontrados");
            }

            if (sector.getTechs() != null) {
                sector.getTechs().stream().filter(tech -> !techs.contains(tech)).forEach(tech -> tech.setSector(null));
            }
            sector.setTechs(new java.util.ArrayList<>(techs));

            Sector finalSector1 = sector;
            techs.forEach(t -> t.setSector(finalSector1));
        }

        sector = sectorRepository.save(sector);
        return sectorMapper.toResponse(sector);
    }

    @Transactional
    public void delete(Long sectorId){
        if (!sectorRepository.existsById(sectorId)) {
            throw new ResourceNotFoundException("Setor não encontrado");
        }

        if (ticketRepository.existsByCategorySectorSectorId(sectorId)) {
            throw new DatabaseException("Não é possível excluir o setor, pois existem chamados vinculados às suas categorias.");
        }

        try {
            sectorRepository.deleteById(sectorId);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: não foi possível excluir o setor.");
        }
    }

    @Transactional
    public List<Category> categories(Long sectorId) {
        Sector sector = findId(sectorId);
        return sector.getCategories();
    }
}

