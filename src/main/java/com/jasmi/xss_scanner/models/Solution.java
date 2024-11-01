package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Solution(String vulnerabilityType, String solution, String implementationSteps, String externalResourceLink, Vulnerability vulnerability) {
        this.vulnerabilityType = vulnerabilityType;
        this.solution = solution;
        this.implementationSteps = implementationSteps;
        this.externalResourceLink = externalResourceLink;
        this.vulnerability = vulnerability;
    }
}
