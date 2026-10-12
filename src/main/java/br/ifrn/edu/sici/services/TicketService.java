package br.ifrn.edu.sici.services;

import java.util.List;

import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.mappers.TicketMapper;
import br.ifrn.edu.sici.models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.TicketRepository;
import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.enums.UserType;
import org.springframework.web.bind.annotation.ResponseStatus;


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

    public List<TicketResponseDTO> listAll(){
       // return ticketRepository.findAll();
        return null;
    }

}

@ResponseStatus(HttpStatus.FORBIDDEN)
class TypeUserDenied extends RuntimeException{
    public TypeUserDenied(String message){
        super(message);
    }
}
