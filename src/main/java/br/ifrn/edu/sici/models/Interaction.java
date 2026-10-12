package br.ifrn.edu.sici.models;

import java.time.LocalTime;

import jakarta.persistence.*;

@Entity 
@Table(name = "tb_interaction")
public class Interaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interactionId;

    @Column(nullable = false, length = 300)
    private String text;

    @Column (nullable = false)
    private LocalTime dateTime;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User author;
}
