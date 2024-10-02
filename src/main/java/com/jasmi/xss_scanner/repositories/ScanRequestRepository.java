package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.ScanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRequestRepository extends JpaRepository<ScanRequest, Long> {
}
