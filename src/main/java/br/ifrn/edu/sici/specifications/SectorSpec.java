package br.ifrn.edu.sici.specifications;

import br.ifrn.edu.sici.models.Sector;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.Locale;

public class SectorSpec {

    public static Specification<Sector> nameContains(String name){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)){
                return null;
            }
            return builder.like(root.get("name"), "%" + name.toLowerCase() + "%");
        };
    }
}
