package com.jasmi.xss_scanner.repositories;

import com.jasmi.xss_scanner.models.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScanResultRepository extends JpaRepository<ScanResult, Long> {

    @Query("SELECT sr FROM ScanResult sr JOIN sr.scanRequest s WHERE s.user.userName = :username")
    List<ScanResult> findByScanRequestUserName(@Param("username") String userName);

//    @Query("SELECT sr FROM ScanResult sr")
//    List<ScanResult> findAllScanResults();
//
//    @Query("SELECT sr FROM ScanResult sr JOIN sr.scanRequest s WHERE sr.id = :id AND s.user.userName = :username")
//    Optional<ScanResult> findByIdAndUserName(@Param("id") Long id, @Param("username") String username);
}


