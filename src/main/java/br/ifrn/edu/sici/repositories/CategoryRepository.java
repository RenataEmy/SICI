package br.ifrn.edu.sici.repositories;


import br.ifrn.edu.sici.models.Category;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository 
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Category findCategoryByCategoryId(Long id);

    Page<Category> findBySectorSectorId(Long sectorId, Pageable pageable);

    
}
