package br.ifrn.edu.sici.repositories;


import br.ifrn.edu.sici.models.Category;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository 
public interface CategoryRepository extends JpaRepository<Category, Long>{

    Category findCategoryByCategoryId(Long id);

    
}
