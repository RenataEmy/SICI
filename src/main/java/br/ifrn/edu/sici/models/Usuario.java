package br.ifrn.edu.sici.models;

import java.util.List;
import jakarta.persistence.*;

@Entity 
@Table(name = "tb_usuario")
public class Usuario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuario_id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private int matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @OneToMany(mappedBy = "usuario")
    private List<Chamado> chamados;
}

enum TipoUsuario{
    TECNICO,
    SOLICITANTE
}