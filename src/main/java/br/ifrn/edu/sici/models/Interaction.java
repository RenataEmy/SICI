package br.ifrn.edu.sici.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "tb_interaction")
@Getter
@Setter
public class Interaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interactionId;

    @Column(nullable = false, length = 300)
    private String text;

    @Column (nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;
}
