import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "tb_setor")
public class Setor{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setor_id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 6,unique = true)
    private String sigla;

    @OneToMany(mappedBy = "setor")
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "setor")
    //verificar como deixar apenas users do tipo tecnico
    private List<Usuario> tecnicos; 

}