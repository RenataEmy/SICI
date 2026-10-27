package br.ifrn.edu.sici.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.ifrn.edu.sici.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

    Optional<User> findByUserId(Long user_id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndUserIdNot(String email, Long userId);

}
