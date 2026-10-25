package br.ifrn.edu.sici.repositories;

import br.ifrn.edu.sici.models.Sector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
public interface SectorRepository extends JpaRepository<Sector, Long>{

    Optional<Sector> findByAcronym(String acronym);

    Page<Sector> findAll(Specification<Sector> spec, Pageable pageable);
}
