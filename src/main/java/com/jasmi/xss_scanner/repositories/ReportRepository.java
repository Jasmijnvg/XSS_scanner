package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}