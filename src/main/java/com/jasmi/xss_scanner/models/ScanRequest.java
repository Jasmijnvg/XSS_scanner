package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ScanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String url;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime requestTimestamp;

    private byte Image;

    @OneToOne
    @JoinColumn(name="scan_result_id")
    private ScanResult scanResult;

}
