package com.jasmi.xss_scanner.dtos.solution;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolutionInputDto {
    private long vulnerabilityId;
    private String vulnerabilityType;
    @NotNull(message = "solution may not be empty")
    private String solution;
    @Lob
    @Column(columnDefinition = "TEXT")
    @NotNull(message = "implementation steps may not be empty")
    private String implementationSteps;
    private String externalResourceLink;
}
