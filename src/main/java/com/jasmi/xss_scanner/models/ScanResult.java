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

    @OneToOne
    @JoinColumn(name="scan_request_id")
    private ScanRequest scanRequest;

    @ManyToMany
    @JoinTable(name = "scan_result_vulnerability",
            joinColumns = @JoinColumn(name = "scan_result_id"),
            inverseJoinColumns = @JoinColumn(name = "vulnerability_id"))
    private List<Vulnerability> vulnerabilities = new ArrayList<>();


}
