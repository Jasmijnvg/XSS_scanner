package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.SolutionInputDto;
import com.jasmi.xss_scanner.dtos.SolutionOutputDto;
import com.jasmi.xss_scanner.models.Solution;
import org.springframework.stereotype.Component;

@Component
public class SolutionMapper {
    public SolutionOutputDto toSolutionDto(Solution solution) {
        var dto = new SolutionOutputDto();

        dto.setId(solution.getId());
        dto.setVulnerabilityType(solution.getVulnerabilityType());
        dto.setSolution(solution.getSolution());
        dto.setImplementationSteps(solution.getImplementationSteps());
        dto.setExternalResourceLinks(solution.getExternalResourceLinks());

        return dto;
    }

    public Solution toSolution(SolutionInputDto dto){
        var solution = new Solution();

        solution.setVulnerabilityType(dto.getVulnerabilityType());
        solution.setSolution(dto.getSolution());
        solution.setImplementationSteps(dto.getImplementationSteps());
        solution.setExternalResourceLinks(dto.getExternalResourceLinks());

        return solution;
    }
}

