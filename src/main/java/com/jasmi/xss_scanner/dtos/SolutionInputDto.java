package com.jasmi.xss_scanner.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SolutionInputDto {
    private String vulnerabilityType;
    private String solution;
    private String implementationSteps;
    private List<String> externalResourceLinks;
}
