package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
}
