package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanResultRepository extends JpaRepository<ScanResult, Long> {

//    WHERE userID --> SecurityContext

}
