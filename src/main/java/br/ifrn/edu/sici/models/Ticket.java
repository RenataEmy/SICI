package br.ifrn.edu.sici.models;

import java.time.LocalDateTime;

import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "tb_ticket")
@Getter 
@Setter 
public class Ticket {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false, length = 50)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Priority priority = Priority.MEDIA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status = Status.ABERTO;

    @Column(updatable = false)
    private LocalDateTime openDate = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime closeDate;

    @Column(nullable = true, length = 300)
    private String solution;

    @ManyToOne
    @JoinColumn(name = "requestId")
    private User request;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "techId", nullable = true)
    private User tech;


}



