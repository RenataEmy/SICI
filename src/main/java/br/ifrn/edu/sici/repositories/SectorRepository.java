package br.ifrn.edu.sici.repositories;

import br.ifrn.edu.sici.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface SectorRepository extends JpaRepository<Sector, Long>{
    
}
