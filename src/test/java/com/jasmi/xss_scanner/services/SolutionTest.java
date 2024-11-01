package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.solution.SolutionInputDto;
import com.jasmi.xss_scanner.dtos.solution.SolutionOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.SolutionMapper;
import com.jasmi.xss_scanner.models.Solution;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.repositories.SolutionRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SolutionTest {

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private SolutionMapper solutionMapper;

    @InjectMocks
    private SolutionService solutionService;

    private Solution solution;
    private Vulnerability vulnerability;
    private SolutionOutputDto solutionOutputDto;
    private SolutionInputDto solutionInputDto;

    @BeforeEach
    public void setUp() {
        vulnerability = new Vulnerability("SQL Injection", "Describes SQL vulnerability", "Example code");

        solution = new Solution();
        solution.setId(1L);
        solution.setVulnerabilityType("SQL Injection");
        solution.setSolution("Fix SQL Injection");
        solution.setImplementationSteps("Steps to fix...");
        solution.setExternalResourceLink("https://resource-link.com");
        solution.setVulnerability(vulnerability);

        solutionOutputDto = new SolutionOutputDto();
        solutionOutputDto.setId(1L);
        solutionOutputDto.setVulnerabilityType("SQL Injection");
        solutionOutputDto.setSolution("Fix SQL Injection");
        solutionOutputDto.setImplementationSteps("Steps to fix...");
        solutionOutputDto.setExternalResourceLink("https://resource-link.com");

        solutionInputDto = new SolutionInputDto();
        solutionInputDto.setVulnerabilityType("SQL Injection");
        solutionInputDto.setSolution("Fix SQL Injection");
        solutionInputDto.setImplementationSteps("Steps to fix...");
        solutionInputDto.setExternalResourceLink("https://resource-link.com");
    }

    @Test
    public void shouldGetAllSolutions() {
        // Arrange
        when(solutionRepository.findAll()).thenReturn(List.of(solution));
        when(solutionMapper.toSolutionDto(solution)).thenReturn(solutionOutputDto);

        // Act
        List<SolutionOutputDto> solutions = solutionService.getAllSolutions();

        // Assert
        assertEquals(1, solutions.size());
        assertEquals("Fix SQL Injection", solutions.get(0).getSolution());
    }

    @Test
    public void shouldGetSolutionById() {
        // Arrange
        when(solutionRepository.findById(1L)).thenReturn(Optional.of(solution));
        when(solutionMapper.toSolutionDto(solution)).thenReturn(solutionOutputDto);

        // Act
        SolutionOutputDto result = solutionService.getSolutionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Fix SQL Injection", result.getSolution());
    }

    @Test
    public void shouldThrowExceptionWhenSolutionNotFoundById() {
        // Arrange
        when(solutionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> solutionService.getSolutionById(1L));
    }

}