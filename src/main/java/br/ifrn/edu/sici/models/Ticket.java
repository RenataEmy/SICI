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

    @Column(nullable = false, length = 300)
    private String solution;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private User request;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "tech_id", nullable = true)
    private User tech;


}



