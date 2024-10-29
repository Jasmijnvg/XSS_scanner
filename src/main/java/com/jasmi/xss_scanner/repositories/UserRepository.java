package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
