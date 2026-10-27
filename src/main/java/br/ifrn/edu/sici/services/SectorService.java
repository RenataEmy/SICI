package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.SectorRequestDTO;
import br.ifrn.edu.sici.dtos.responses.SectorResponseDTO;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.mappers.SectorMapper;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.Sector;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.SectorRepository;
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

    //POST, GET (lista e por id), PUT, DELETE em /setores

    private final SectorRepository sectorRepository;
    private final UserService userService;
    private final SectorMapper sectorMapper;
    private final TicketService ticketService;
    private final CategoryService categoryService;

    public SectorService(
            SectorRepository sectorRepository,
            UserService userService,
            SectorMapper sectorMapper,
            TicketService ticketService,
            CategoryService categoryService,
            SectorSpec sectorSpec
    ){
        this.sectorRepository = sectorRepository;
        this.userService = userService;
        this.sectorMapper = sectorMapper;
        this.ticketService = ticketService;
        this.categoryService = categoryService;
    }

    public SectorResponseDTO create(SectorRequestDTO data, Long adminId){
        User admin = userService.findById(adminId);

        if (!admin.getType().equals(UserType.ADMINISTRADOR)){
            throw new RuleConflit("Apenas usuário do tipo administrador pode cadastrar um setor");
        }

        Optional<Sector> entity = sectorRepository.findByAcronym(data.acronym());

        if(entity.get().getAcronym().equals(data.acronym())){
            throw new RuleConflit("Já existe setor cadastrado com essa sigla");
        }

        Sector newSector = sectorMapper.toEntity(data);

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
            List<Category> categories = categoryService.findAll(data.categoriesId());

            Sector finalSector = sector;
            categories.forEach(category -> category.setSector(finalSector));

            sector.getCategories().clear();
            sector.getCategories().addAll(categories);
        }

        if (data.techsId() != null) {
            List<User> techs = userService.findAllTechs(data.techsId());

            sector.getTechs().clear();
            sector.getTechs().addAll(techs);

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

        if (ticketService.verify(sectorId)) {
            throw new DatabaseException("Não é possível excluir o setor, pois existem chamados vinculados às suas categorias.");
        }

        try {
            sectorRepository.deleteById(sectorId);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: não foi possível excluir o setor.");
        }
    }
}

