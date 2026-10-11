package br.ifrn.edu.sici.services;

import java.util.List;

import br.ifrn.edu.sici.models.Category;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.TicketRepository;
import br.ifrn.edu.sici.services.UserService;
import br.ifrn.edu.sici.services.CategoryService;
import br.ifrn.edu.sici.DTOs.TicketRequestDTO;
import br.ifrn.edu.sici.enums.UserType;


@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public TicketService(TicketRepository ticketRepository, UserService userService, CategoryService categoryService){
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public Ticket create(TicketRequestDTO data, Long requestId){
        User request = userService.findById(requestId);

        if(request.getType() != UserType.SOLICITANTE){
            throw new TypeUserDenied("O usuário não tem o perfil SOLICITANTE.");
        }

        Category category = categoryService.findById(data.getCategoryId());

        // return ticketRepository.save(data, request_id); // Needs implementation to convert DTO to Entity
        return null; 
    }

    public List<Ticket> listAll(){
        return ticketRepository.findAll();
    }

}


// Estudar a criação de exceções
class TypeUserDenied extends RuntimeException{
    public TypeUserDenied(String message){
        super(message);
    }
}
