package br.ifrn.edu.sici.models;

import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "tb_sector")
public class Sector{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectorId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 6,unique = true)
    private String acronym;

    @OneToMany(mappedBy = "sector")
    private List<Category> categories;

    @OneToMany(mappedBy = "sector")
    //verificar como deixar apenas users do tipo tecnico
    private List<User> techs; 

}