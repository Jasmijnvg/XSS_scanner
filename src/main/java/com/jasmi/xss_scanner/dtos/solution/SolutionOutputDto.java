package com.jasmi.xss_scanner.dtos.solution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolutionOutputDto {
    private long id;
    private long vulnerabilityId;
    private String vulnerabilityType;
    private String solution;
    private String implementationSteps;
    private String externalResourceLink;
}
