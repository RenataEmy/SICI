package br.ifrn.edu.sici.repositories;

import br.ifrn.edu.sici.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository 
public interface SectorRepository extends JpaRepository<Sector, Long>, JpaSpecificationExecutor<Sector>{

    Optional<Sector> findByAcronym(String acronym);
    boolean existsByAcronymAndSectorIdNot(String acronym, Long sectorId);

}
