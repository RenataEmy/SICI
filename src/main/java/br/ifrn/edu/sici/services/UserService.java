package br.ifrn.edu.sici.services;

import org.springframework.stereotype.Service;
import br.ifrn.edu.sici.models.User;
import br.ifrn.edu.sici.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service 
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não existe"));
    }
}
