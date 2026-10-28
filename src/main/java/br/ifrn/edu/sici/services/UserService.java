package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.UserRequestDTO;
import br.ifrn.edu.sici.dtos.responses.UserResponseDTO;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.filters.UserFilter;
import br.ifrn.edu.sici.mappers.UserMapper;
import br.ifrn.edu.sici.mappers.TicketMapper;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.models.Sector;
import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.repositories.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.UserRepository;
import br.ifrn.edu.sici.exceptions.ResourceNotFoundException;
import br.ifrn.edu.sici.exceptions.RuleConflit;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service 
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SectorService sectorService;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       SectorService sectorService, TicketRepository ticketRepository, TicketMapper ticketMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.sectorService = sectorService;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO data){
        boolean isEmail = userRepository.existsByEmail(data.email());

        if(isEmail){
            throw new RuleConflit("Já existe usuário com esse e-mail cadastrado");
        }

        User newUser = userMapper.toEntity(data);

        if (data.sectorId() != null) newUser.setSector(sectorService.findId(data.sectorId()));
        if (data.ticketsId() != null) {
            if (newUser.getType() != UserType.TECNICO) throw new RuleConflit("Somente usuários técnicos podem receber chamados");
            List<Ticket> tickets = ticketRepository.findAllById(data.ticketsId());
            if (tickets.size() != data.ticketsId().size()) throw new ResourceNotFoundException("Um ou mais chamados não foram encontrados");
            newUser.setTickets(new java.util.ArrayList<>(tickets));
            for (Ticket ticket : tickets) ticket.setTech(newUser);
        }

        newUser = userRepository.save(newUser);

        return userMapper.toResponse(newUser);

    }

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));
    }

    public UserResponseDTO get(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));
        return userMapper.toResponse(user);
    }


    public List<User> findAllTechs(List<Long> techsId){
        List<User> users = userRepository.findAllById(techsId);
        if (users.size() != techsId.size() || users.stream().anyMatch(user -> user.getType() != UserType.TECNICO)) {
            throw new ResourceNotFoundException("Um ou mais técnicos não foram encontrados");
        }
        return users;
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

        boolean isEmail = userRepository.existsByEmailAndUserIdNot(data.email(), userId);

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
            if (user.getType() != UserType.TECNICO) throw new RuleConflit("Somente usuários técnicos podem receber chamados");
            List<Ticket> tickets = ticketRepository.findAllById(data.ticketsId());
            if (tickets.size() != data.ticketsId().size()) throw new ResourceNotFoundException("Um ou mais chamados não foram encontrados");

            if (user.getTickets() != null) {
                user.getTickets().stream().filter(ticket -> !tickets.contains(ticket)).forEach(ticket -> ticket.setTech(null));
            }
            for (Ticket ticket : tickets) ticket.setTech(user);

            user.setTickets(new java.util.ArrayList<>(tickets));
        }

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public Page<TicketResponseDTO> tickets(Long userId, Status status, Pageable pageable) {
        findById(userId);
        return ticketRepository.findByUserAndStatus(userId, status, pageable).map(ticketMapper::toResponseDTO);
    }
}
