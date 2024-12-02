package com.port.retapi.config.security.repository;

import com.port.retapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositorySecurity extends JpaRepository<User, Long> {

    // Este método se utiliza
    Optional<User> findByNickname(String nickname);



}
