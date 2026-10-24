package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.TicketResolvedDTO;
import br.ifrn.edu.sici.dtos.responses.TicketDetailResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.filters.TicketFilter;
import br.ifrn.edu.sici.mappers.TicketMapper;
import br.ifrn.edu.sici.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.TicketRepository;
import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.enums.UserType;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TicketMapper ticketMapper;

    public TicketService(
            TicketRepository ticketRepository,
            UserService userService,
            CategoryService categoryService,
            TicketMapper ticketMapper
    ){
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.ticketMapper = ticketMapper;
    }

    public TicketResponseDTO create(TicketRequestDTO data, Long requestId){
        User request = userService.findById(requestId);

        if(request.getType() != UserType.SOLICITANTE){
            throw new TypeUserDenied("O usuário não tem o perfil SOLICITANTE.");
        }

        Category category = categoryService.findById(data.categoryId());

        Ticket entity = ticketMapper.toEntity(data);

        entity.setRequest(request);
        entity.setCategory(category);

        if (data.priority() != null){
            entity.setPriority(data.priority());
        }

        Ticket ticket = ticketRepository.save(entity);

        return ticketMapper.toResponseDTO(ticket);

    }

    public Page<TicketResponseDTO> listAll(TicketFilter ticketFilter, Pageable pageable){
        Page<Ticket> ticketsPage = ticketRepository.findAll(ticketFilter.toSpecification(), pageable);

        if (ticketsPage.isEmpty()){
            throw new TicketNotFound("Nenhum chamado localizado");
        }

        return ticketsPage.map(ticketMapper::toResponseDTO);
    }

    public TicketDetailResponseDTO getTicket(Long ticketId){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new TicketNotFound("Chamado não localizado com esse identificador"));

        return ticketMapper.toDetailDTO(ticket);
    }

    public TicketResponseDTO takeTicket(Long ticketId, Long techId){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new TicketNotFound("Chamado não localizado com esse identificador"));

        User tech = userService.findById(techId);

        Long sectorCategory = ticket.getCategory().getSector().getSectorId();
        Long sectorTech = tech.getSector().getSectorId();

        if(tech.getType() != UserType.TECNICO){
            throw new TypeUserDenied("O usuário não tem o perfil TÉCNICO.");
        }

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

    public TicketResolvedDTO resolve(Long ticketId, TicketResolvedDTO data){
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new TicketNotFound("Chamado não localizado com esse identificador"));

        User tech = userService.findById(data.techId());

        if (!tech.getUserId().equals(data.techId())){
            throw new RuleConflit("Apenas o técnico responsável pode encerrar o chamado");
        }

        ticket.setSolution(data.solution());
        ticket.setCloseDate(LocalDateTime.now());

        ticket = ticketRepository.save(ticket);

        return ticketMapper.toResolvedDTO(ticket);
    }

}

@ResponseStatus(HttpStatus.FORBIDDEN)
class TypeUserDenied extends RuntimeException{
    public TypeUserDenied(String message){
        super(message);
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class TicketNotFound extends RuntimeException{
    public TicketNotFound(String message){ super(message);}
}

@ResponseStatus(HttpStatus.CONFLICT)
class RuleConflit extends RuntimeException{
    public RuleConflit(String message){ super(message);}
}
