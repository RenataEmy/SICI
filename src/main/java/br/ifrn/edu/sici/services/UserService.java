package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.UserRequestDTO;
import br.ifrn.edu.sici.dtos.responses.UserResponseDTO;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.filters.UserFilter;
import br.ifrn.edu.sici.mappers.UserMapper;
import br.ifrn.edu.sici.models.Sector;
import br.ifrn.edu.sici.models.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service 
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SectorService sectorService;
    private final TicketService ticketService;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       SectorService sectorService, TicketService ticketService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.sectorService = sectorService;
        this.ticketService = ticketService;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO data){
        boolean isEmail = userRepository.existsByEmail(data.email());

        if(isEmail){
            throw new RuleConflit("Já existe usuário com esse e-mail cadastrado");
        }

        User newUser = userMapper.toEntity(data);

        newUser = userRepository.save(newUser);

        return userMapper.toResponse(newUser);

    }

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não existe"));
    }

    public UserResponseDTO get(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não existe"));
        return userMapper.toResponse(user);
    }


    public List<User> findAllTechs(List<Long> techsId){
        return userRepository.findAllById(techsId);
    }

    public Page<UserResponseDTO> findWithFilters(UserFilter userFilter, Pageable pageable){
        Page<User> usersPage = userRepository.findAll(userFilter.toSpecification(), pageable);
        if(usersPage.isEmpty()){
            throw new ResourceNotFoundException("Nenhum usuário localizado");
        }

        return usersPage.map(userMapper::toResponse);

    }

    @Transactional
    public UserResponseDTO change(Long userId, UserRequestDTO data) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean isEmail = userRepository.existsByEmail(data.email());

        if(isEmail){
            throw new RuleConflit("Já existe usuário com esse e-mail cadastrado");
        }

        user.setName(data.name());
        user.setEmail(data.email());
        user.setRegistration(data.registration());

        if (data.type() != null) {
            user.setType(UserType.valueOf(data.type().toUpperCase()));
        }

        if (data.sectorId() != null) {
            Sector sector = sectorService.findId(data.sectorId());
            user.setSector(sector);
        } else {
            user.setSector(null);
        }

        if (data.ticketsId() != null) {
            List<Ticket> tickets = ticketService.findAll(data.ticketsId());

            //tickets.forEach(ticket -> ticket.setTech(userId));

            user.getTickets().clear();
            user.getTickets().addAll(tickets);
        }

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }


}
