package br.ifrn.edu.sici.filters;
import static br.ifrn.edu.sici.specifications.UserSpec.*;
import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.models.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class UserFilter {
    private String name;
    private UserType type;
    private Long sectorId;

    public Specification<User> toSpecification(){
        return nameEquals(name)
                .and(userTypeEquals(type))
                .and(sectorEquals(sectorId));
    }
}
