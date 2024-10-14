package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String vulnerabilityType;
    private String solution;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String implementationSteps;

    private String externalResourceLink;

    @ManyToOne
    @JoinColumn(name = "vulnerability_id")
    private Vulnerability vulnerability;
}
