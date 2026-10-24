package br.ifrn.edu.sici.models;

import java.util.List;

import br.ifrn.edu.sici.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity 
@Table(name = "tb_user")
@Getter 
@Setter
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private int registration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @OneToMany(mappedBy = "tech")
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "sectorId")
    private Sector sector;
}

