package br.ifrn.edu.sici.specifications;

import br.ifrn.edu.sici.models.Sector;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Locale;

@Component
public class SectorSpec {

    public static Specification<Sector> nameContains(String name){
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(name)){
                return null;
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
