package br.ifrn.edu.sici.models;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name = "tb_category")
@Getter
@Setter
public class Category {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    @ManyToOne 
    @JoinColumn(name = "sectorId")
    private Sector sector;

    @OneToMany(mappedBy = "category")
    private List<Ticket> tickets;

}
