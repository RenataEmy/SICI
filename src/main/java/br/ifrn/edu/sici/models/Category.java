package br.ifrn.edu.sici.models;
import java.util.List;

import jakarta.persistence.*;

@Entity 
@Table(name = "tb_category")
public class Category {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    @ManyToOne 
    @JoinColumn(name = "setorId")
    private Sector sector;

    @OneToMany(mappedBy = "category")
    private List<Ticket> tickets;

}
