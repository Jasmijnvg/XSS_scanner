package com.jasmi.xss_scanner.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SolutionInputDto {
    @NotNull(message = "vulnerability type may not be empty")
    private String vulnerabilityType;
    @NotNull(message = "solution may not be empty")
    private String solution;
    @NotNull(message = "implementation steps may not be empty")
    private String implementationSteps;
    private List<String> externalResourceLinks;
}
