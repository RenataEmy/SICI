package br.ifrn.edu.sici.services;

import br.ifrn.edu.sici.models.Interaction;
import br.ifrn.edu.sici.repositories.InteractionRepository;
import org.springframework.stereotype.Service;

@Service
public class InteractionService {

    private final InteractionRepository interactionRepository;

    public InteractionService(InteractionRepository interactionRepository){
        this.interactionRepository = interactionRepository;
    }

    public Interaction create(Interaction interaction){
        return interactionRepository.save(interaction);
    }
}
