package br.ifrn.edu.sici.repositories;

import java.awt.print.Pageable;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ifrn.edu.sici.enums.UserType;
import br.ifrn.edu.sici.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUserId(Long user_id);

    boolean existsByEmail(String email);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
