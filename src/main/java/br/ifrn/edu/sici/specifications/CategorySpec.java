package br.ifrn.edu.sici.specifications;

import br.ifrn.edu.sici.models.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class CategorySpec {

    public static Specification<Category> nameContains(String name){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)){
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Category> sectorEquals(Long sectorId){
        return (root, query, builder) -> {
            if(ObjectUtils.isEmpty(sectorId)){
                return null;
            }
            return builder.equal(root.join("sector").get("sectorId"), sectorId);
        };
    }
}
