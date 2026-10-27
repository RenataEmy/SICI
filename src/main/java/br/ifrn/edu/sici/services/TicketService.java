package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.InteractionRequestDTO;
import br.ifrn.edu.sici.dtos.requests.TicketResolvedDTO;
import br.ifrn.edu.sici.dtos.responses.InteractionResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketDetailResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.filters.TicketFilter;
import br.ifrn.edu.sici.mappers.TicketMapper;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.Interaction;
import br.ifrn.edu.sici.exceptions.ResourceNotFoundException;
import br.ifrn.edu.sici.exceptions.RuleConflit;
import br.ifrn.edu.sici.exceptions.TypeUserDenied;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.TicketRepository;
import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.enums.UserType;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TicketMapper ticketMapper;
    private final InteractionService interactionService;

    public TicketService(
            TicketRepository ticketRepository,
            UserService userService,
            CategoryService categoryService,
            TicketMapper ticketMapper,
            InteractionService interactionService
    ){
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.ticketMapper = ticketMapper;
        this.interactionService = interactionService;
    }

    public boolean verify(Long sectorId){
        return ticketRepository.existsByCategorySectorSectorId(sectorId);
    }

    @Transactional
    public TicketResponseDTO create(TicketRequestDTO data){
        User request = userService.findById(data.requestId());

        if(request.getType() != UserType.SOLICITANTE){
            throw new TypeUserDenied("O usuário não tem o perfil SOLICITANTE.");
        }

        Category category = categoryService.findById(data.categoryId());

        Ticket entity = ticketMapper.toEntity(data);

        entity.setRequest(request);
        entity.setCategory(category);

        if (data.priority() != null){
            entity.setPriority(data.priority());
        } else {
            entity.setPriority(Priority.MEDIA);
        }

        Ticket ticket = ticketRepository.save(entity);

        return ticketMapper.toResponseDTO(ticket);

    }

    public TicketResponseDTO create(TicketRequestDTO data, Long requestId) {
        TicketRequestDTO request = new TicketRequestDTO(data.title(), data.description(), data.location(), data.priority(), data.categoryId(), requestId);
        return create(request);
    }

    public List<Ticket> findAll(List<Long> ticketsId){
        List<Ticket> tickets = ticketRepository.findAllById(ticketsId);
        return tickets;
    }

    public Page<TicketResponseDTO> listAll(TicketFilter ticketFilter, Pageable pageable){
        Page<Ticket> ticketsPage = ticketRepository.findAll(ticketFilter.toSpecification(), pageable);

        if (ticketsPage.isEmpty()){
            throw new ResourceNotFoundException("Nenhum chamado localizado");
        }

        return ticketsPage.map(ticketMapper::toResponseDTO);
    }

    public TicketDetailResponseDTO get(Long ticketId){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não localizado com esse identificador"));

        return ticketMapper.toDetailDTO(ticket);
    }

    @Transactional
    public TicketResponseDTO take(Long ticketId, Long techId){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não localizado com esse identificador"));

        User tech = userService.findById(techId);

        if(tech.getType() != UserType.TECNICO){
            throw new TypeUserDenied("O usuário não tem o perfil TÉCNICO.");
        }

        if (tech.getSector() == null || ticket.getCategory().getSector() == null) {
            throw new RuleConflit("O técnico e a categoria do chamado devem estar vinculados a um setor");
        }

        Long sectorCategory = ticket.getCategory().getSector().getSectorId();
        Long sectorTech = tech.getSector().getSectorId();

        if (ticket.getStatus() != Status.ABERTO){
            throw new RuleConflit("Não é possível assumir um chamado que esteja em andamento");
        }

        if (!sectorCategory.equals(sectorTech)){
            throw new RuleConflit("Não é possível assumir um chamado de outro setor");
        }

        ticket.setTech(tech);
        ticket.setStatus(Status.EM_ATENDIMENTO);

        ticket = ticketRepository.save(ticket);
        return ticketMapper.toResponseDTO(ticket);

    }

    @Transactional
    public TicketResponseDTO resolve(Long ticketId, TicketResolvedDTO data){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não localizado com esse identificador"));

        User tech = userService.findById(data.techId());

        if (tech.getType() != UserType.TECNICO || ticket.getTech() == null
                || !ticket.getTech().getUserId().equals(tech.getUserId())){
            throw new RuleConflit("Apenas o técnico responsável pode encerrar o chamado");
        }

        if (ticket.getStatus() != Status.EM_ATENDIMENTO) {
            throw new RuleConflit("Apenas chamados em atendimento podem ser resolvidos");
        }

        ticket.setSolution(data.solution());
        ticket.setCloseDate(LocalDateTime.now());
        ticket.setStatus(Status.RESOLVIDO);

        ticket = ticketRepository.save(ticket);

        return ticketMapper.toResponseDTO(ticket);
    }

    @Transactional
    public String cancel(Long ticketId, Long requestId){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não localizado com esse identificador"));

        userService.findById(requestId);

        if(!ticket.getRequest().getUserId().equals(requestId)){
            throw new RuleConflit("Apenas o solicitante pode cancelar este chamado");
        }

        if (ticket.getStatus() != Status.ABERTO){
            throw new RuleConflit("Não é possível cancelar um chamado que esteja em andamento");
        }

        ticket.setStatus(Status.CANCELADO);

        ticket = ticketRepository.save(ticket);

        return "Chamado cancelado com sucesso";
    }

    @Transactional
    public InteractionResponseDTO interactions(Long ticketId, InteractionRequestDTO data){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não localizado com esse identificador"));

        User user = userService.findById(data.userId());

        if (!user.getUserId().equals(ticket.getRequest().getUserId()) &&
                (ticket.getTech() == null || !user.getUserId().equals(ticket.getTech().getUserId()))){
            throw new RuleConflit("Apenas o solicitante e o técnico atribuídos podem registrar interações no chamado");
        }

        if(ticket.getStatus().equals(Status.CANCELADO) || ticket.getStatus().equals(Status.RESOLVIDO)){
            throw new RuleConflit("Chamados RESOLVIDOS ou CANCELADOS não aceitam alterações nem novas interações");
        }

        Interaction entity = ticketMapper.toEntityInteraction(data);

        entity.setAuthor(user);
        entity.setTicket(ticket);

        Interaction interaction = interactionService.create(entity);

        return ticketMapper.toInteractionDTO(interaction);

    }
}
