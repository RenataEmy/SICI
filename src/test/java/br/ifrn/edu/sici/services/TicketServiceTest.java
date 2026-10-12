package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.mappers.TicketMapper;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.TicketRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TicketServiceTest {

    // Transformar depois em métodos que chamam os testes dos repositories

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("Should create ticket successfully when everything is ok.")
    void createTicketSuccess() throws Exception{
        // 1. PREPARAR OS DADOS
        TicketRequestDTO requestDTO = new TicketRequestDTO(
                "Piso molhado",
                "Piso foi molhado por aluno",
                "Sala 47",
                //Na regra de negócio STATUS, PRIORITY e TECH não são obrigatórios
                null,
                1L
        );
        Long requestId = 1L;

        User mockUser = new User();
        mockUser.setUserId(requestId);
        mockUser.setType(UserType.SOLICITANTE);

        Category mockCategory = new Category();
        mockCategory.setCategoryId(1L);
        mockCategory.setName("Limpeza");

        Ticket mockEntity = new Ticket(); // Não precisa setar dados pois é o rquestDTO que passará essas informações
        Ticket mockSavedTicket = new Ticket();

        TicketResponseDTO responseDTO = new TicketResponseDTO(
                1L,
                "Piso molhado",
                "Piso foi molhado por aluno",
                "Sala 47",
                Priority.MEDIA,
                Status.ABERTO,
                LocalDateTime.of(2026, 10, 12, 10, 0),
                null,
                null,
                1L,
                1L,
                1L
        );

        // 2. CRIAR CONDIÇÕES
        //Pegar o usuário
        when(userService.findById(requestId)).thenReturn(mockUser);
        //Pegar a categoria
        when(categoryService.findById(requestDTO.categoryId())).thenReturn(mockCategory);
        //Mapear o dto para entity
        when(ticketMapper.toEntity(requestDTO)).thenReturn(mockEntity);
        //Setar demais dados na entidade

        // Chamar o repository para salvar
        when(ticketRepository.save(mockEntity)).thenReturn(mockSavedTicket);
        // Retornar os responseDTO
        when(ticketMapper.toResponseDTO(mockSavedTicket)).thenReturn(responseDTO);

        // 3. EXECUTAR O MÉTODO

        TicketResponseDTO result = ticketService.create(requestDTO, requestId);

        // 4. VERIFICAR
        assertNotNull(result);
        assertEquals(responseDTO, result);

        assertEquals(mockUser, mockEntity.getRequest());
        assertEquals(mockCategory, mockEntity.getCategory());

        verify(ticketRepository, times(1)).save(mockEntity);

    }

    @Test
    @DisplayName("Should create ticket failed when user not is tech")
    void createTicketFailed(){

    }
}