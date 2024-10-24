package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ScanResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resultData;

    @OneToOne
    @JoinColumn(name="scan_request_id")
    private ScanRequest scanRequest;

    @ManyToMany(mappedBy = "scanResults")
    private List<Vulnerability> vulnerabilities = new ArrayList<>();
}
