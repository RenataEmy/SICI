package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.repositories.CategoryRepository;
import br.ifrn.edu.sici.repositories.UserRepository;
import br.ifrn.edu.sici.dtos.requests.CategoryRequestDTO;
import br.ifrn.edu.sici.dtos.responses.CategoryResponseDTO;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.filters.CategoryFilter;
import br.ifrn.edu.sici.mappers.CategoryMapper;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.exceptions.ResourceNotFoundException;
import br.ifrn.edu.sici.exceptions.RuleConflit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SectorService sectorService;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, SectorService sectorService,
                           UserRepository userRepository, CategoryMapper categoryMapper){
        this.categoryRepository = categoryRepository;
        this.sectorService = sectorService;
        this.userRepository = userRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category findById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Categoria não existe"));
    }

//    public List<Category> findAll(List<Long> categoriesId){
//        return categoryRepository.findAllById(categoriesId);
//    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO data) {
        User admin = userRepository.findById(data.adminId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (admin.getType() != UserType.ADMINISTRADOR) throw new RuleConflit("Apenas usuário administrador pode cadastrar categorias");

        Category category = new Category();
        category.setName(data.name());
        category.setDescription(data.description());
        category.setSector(sectorService.findId(data.sectorId()));

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public Page<CategoryResponseDTO> list(CategoryFilter filter, Pageable pageable) {
        return categoryRepository.findAll(filter.toSpecification(), pageable).map(categoryMapper::toResponse);
    }

    public CategoryResponseDTO get(Long id) {
        return categoryMapper.toResponse(findById(id));
    }

    @Transactional
    public CategoryResponseDTO change(Long id, CategoryRequestDTO data) {
        Category category = findById(id);
        User admin = userRepository.findById(data.adminId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário administrador não encontrado"));
        if (admin.getType() != UserType.ADMINISTRADOR) {
            throw new RuleConflit("Apenas usuário administrador pode alterar categorias");
        }
        category.setName(data.name());
        category.setDescription(data.description());
        category.setSector(sectorService.findId(data.sectorId()));
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        Category category = findById(id);
        if (category.getTickets() != null && !category.getTickets().isEmpty()){
            throw new RuleConflit("Não é possível excluir categoria vinculada a chamados");
        }
        try {
            categoryRepository.delete(category);
        }
        catch (DataIntegrityViolationException ex) {
            throw new RuleConflit("Não é possível excluir categoria vinculada a chamados");
        }
    }
}
