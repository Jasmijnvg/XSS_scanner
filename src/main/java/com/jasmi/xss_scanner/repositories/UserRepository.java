package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);

    Optional<User> findByUserNameAndPassword(String username, String password);

}
