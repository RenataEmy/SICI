package br.ifrn.edu.sici.specifications;

import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.models.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class UserSpec {

    public static Specification<User> nameEquals(String name){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)){
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<User> userTypeEquals(UserType type){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(type)){
                return null;
            }
            return builder.equal(root.get("type"), type);
        };
    }

    public static Specification<User> sectorEquals(Long sectorId){
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(sectorId)){
                return null;
            }
            return builder.equal(root.join("sector").get("sectorId"), sectorId);
        };
    }
}
