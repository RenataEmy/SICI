package br.ifrn.edu.sici.repositories;

import br.ifrn.edu.sici.models.Interaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface InteractionRepository extends JpaRepository<Interaction, Long>{

    
} 
