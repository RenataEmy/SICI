package br.ifrn.edu.sici.models;

import java.time.LocalTime;

import jakarta.persistence.*;

@Entity 
@Table(name = "tb_interacao")
public class Interacao {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interacao_id;

    @Column(nullable = false, length = 300)
    private String texto;

    @Column (nullable = false)
    private LocalTime dataHora;

    @OneToOne 
    private Chamado chamado;

    @OneToOne 
    private Usuario autor;
}
