package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String vulnerabilityType;
    private String solution;
    @Column(columnDefinition="varchar(10000)")
    private String implementationSteps;

    private String externalResourceLink;

    @ManyToOne
    @JoinColumn(name = "vulnerability_id")
    private Vulnerability vulnerability;
}
