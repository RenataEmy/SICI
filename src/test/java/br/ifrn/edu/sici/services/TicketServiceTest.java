package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.dtos.requests.TicketRequestDTO;
import br.ifrn.edu.sici.dtos.responses.InteractionResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketDetailResponseDTO;
import br.ifrn.edu.sici.dtos.responses.TicketResponseDTO;
import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.mappers.TicketMapper;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.Interaction;
import br.ifrn.edu.sici.models.Ticket;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.TicketRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Spy
    private TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("Should create ticket successfully when everything is ok.")
    void createTicketSuccess() throws Exception {
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

        System.out.println("Dados retornados: " + result.toString());

        // 4. VERIFICAR
        assertNotNull(result);
        assertEquals(responseDTO, result);

        assertEquals(mockUser, mockEntity.getRequest());
        assertEquals(mockCategory, mockEntity.getCategory());

        verify(ticketRepository, times(1)).save(mockEntity);

    }

    @Test
    @DisplayName("Should create ticket failed when user not is tech")
    void createTicketFailed() {

    }

    @Test
    void getTicketSucess() {
        Long ticketId = 1L;

        User mockAuthor = new User();
        mockAuthor.setUserId(1L);
        mockAuthor.setType(UserType.SOLICITANTE);

        Category mockCategory = new Category();
        mockCategory.setCategoryId(1L);

        Ticket mockTicket = new Ticket();
        mockTicket.setTicketId(ticketId);
        mockTicket.setTitle("Piso molhado");
        mockTicket.setDescription("Piso foi molhado por aluno");
        mockTicket.setLocation("Sala 47");
        mockTicket.setPriority(Priority.MEDIA);
        mockTicket.setStatus(Status.ABERTO);
        mockTicket.setOpenDate(LocalDateTime.of(2026, 10, 12, 10, 0)); // Faltava isso
        mockTicket.setCategory(mockCategory);
        mockTicket.setRequest(mockAuthor);

        Interaction mockInteraction = new Interaction();
        mockInteraction.setInteractionId(1L);
        mockInteraction.setText("Interação 1");
        mockInteraction.setDateTime(LocalDateTime.of(2026, 10, 15, 10, 0));
        mockInteraction.setAuthor(mockAuthor);
        mockInteraction.setTicket(mockTicket);

        mockTicket.setInteractions(List.of(mockInteraction));

        InteractionResponseDTO responseInteDTO = new InteractionResponseDTO(
                mockInteraction.getInteractionId(),
                mockInteraction.getText(),
                mockInteraction.getDateTime(),
                mockInteraction.getAuthor().getUserId()
        );

        TicketDetailResponseDTO responseDTO = new TicketDetailResponseDTO(
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
                null,
                List.of(responseInteDTO)
        );

        when(ticketRepository.findByTicketId(ticketId)).thenReturn(Optional.of(mockTicket));

        TicketDetailResponseDTO result = ticketService.getTicket(ticketId);

        System.out.println(result.toString());
        assertNotNull(result);
        assertEquals(responseDTO, result);

        verify(ticketRepository, times(1)).findByTicketId(ticketId);


    }


    @Test
    @DisplayName("Should not found ticket with id")
    void getTicketFailed(){
        Long ticketId = 2L;

        when(ticketRepository.findByTicketId(ticketId)).thenReturn(Optional.empty());

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TicketDetailResponseDTO result = ticketService.getTicket(ticketId);
        });


        Assertions.assertEquals("Chamado não localizado com esse identificador", thrown.getMessage());

        verify(ticketRepository, times(1)).findByTicketId(ticketId);


    }

    @Test
    @DisplayName("Should be tech to assum ticket with success")
    void takeTicketSuccess() {
    }
}