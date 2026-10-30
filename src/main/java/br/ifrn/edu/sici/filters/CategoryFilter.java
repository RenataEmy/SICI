package br.ifrn.edu.sici.filters;

import br.ifrn.edu.sici.models.Category;
import br.ifrn.edu.sici.specifications.CategorySpec;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class CategoryFilter {
    private String name;
    private Long sectorId;

    public Specification<Category> toSpecification() {
        return CategorySpec.nameContains(name).and(CategorySpec.sectorEquals(sectorId));
    }
}
