package br.ifrn.edu.sici.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "tb_chamado")
@Getter 
@Setter 
public class Chamado {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chamado_id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 250)
    private String descricao;

    @Column(nullable = false, length = 50)
    private String local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Prioridade prioridade = Prioridade.MEDIA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Status status = Status.ABERTO;

    @Column(updatable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime dataFechamento;

    @Column(nullable = false, length = 300)
    private String solucao;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = true)
    private Usuario tecnico;


}

enum Prioridade{
    BAIXA,
    MEDIA, 
    ALTA
}

enum Status{
    ABERTO,
    EM_ATENDIMENTO,
    RESOLVIDO,
    CANCELADO
}