package br.ifrn.edu.sici.models;
import java.util.List;

import jakarta.persistence.*;

@Entity 
@Table(name = "tb_categoria")
public class Categoria {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoria_id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 200)
    private String descricao;

    @ManyToOne 
    @JoinColumn(name = "setor_id")
    private Setor setor;

    @OneToMany(mappedBy = "categoria")
    private List<Chamado> chamados;

}
