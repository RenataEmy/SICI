package br.ifrn.edu.sici.specifications;

import br.ifrn.edu.sici.models.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class CategorySpec {

    public static Specification<Category> nameEquals(String name){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)){
                return null;
            }
            return builder.equal(root.get("name"), name);
        };
    }

    public static Specification<Category> sectorEquals(Long sectorId){
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(sectorId)){
                return null;
            }
            return builder.equal(root.get("sectorId"), sectorId);
        };
    }
}
