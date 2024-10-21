package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.SolutionInputDto;
import com.jasmi.xss_scanner.dtos.SolutionOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.SolutionMapper;
import com.jasmi.xss_scanner.models.Solution;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.repositories.SolutionRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final SolutionMapper solutionMapper;
    private final VulnerabilityRepository vulnerabilityRepository;

    public SolutionService(SolutionRepository solutionRepository, SolutionMapper solutionMapper, VulnerabilityRepository vulnerabilityRepository) {
        this.solutionRepository = solutionRepository;
        this.solutionMapper = solutionMapper;
        this.vulnerabilityRepository = vulnerabilityRepository;
    }

    public List<SolutionOutputDto> getAllSolutions() {
        return solutionRepository.findAll()
                .stream()
                .map(solutionMapper::toSolutionDto)
                .collect(Collectors.toList());
    }

    public SolutionOutputDto getSolutionById(long id) {
        Optional<Solution> optionalSolution = solutionRepository.findById(id);
        if (optionalSolution.isPresent()) {
            Solution s = optionalSolution.get();
            return solutionMapper.toSolutionDto(s);
        } else {
            throw new RecordNotFoundException("Solution " + id + " not found");
        }
    }

//    public SolutionOutputDto saveSolution(SolutionInputDto solution) {
//        Solution s = solutionMapper.toSolution(solution);
//        Solution savedSolution = solutionRepository.save(s);
//
//        return solutionMapper.toSolutionDto(savedSolution);
//    }

    public SolutionOutputDto saveSolution(SolutionInputDto solutionDto) {
        Vulnerability vulnerability = vulnerabilityRepository.findByName(solutionDto.getVulnerabilityType())
                .orElseThrow(() -> new RecordNotFoundException("Vulnerability not found for type: " + solutionDto.getVulnerabilityType()));

        Solution solution = solutionMapper.toSolution(solutionDto);

        solution.setVulnerability(vulnerability);

        Solution savedSolution = solutionRepository.save(solution);

        return solutionMapper.toSolutionDto(savedSolution);
    }


    public SolutionOutputDto updateSolution(long id, SolutionInputDto updatedSolution) {
        if (!solutionRepository.existsById(id)) {
            throw new RecordNotFoundException("Solution " + id + " not found");
        }
        Solution s = solutionMapper.toSolution(updatedSolution);
        s.setId(id);
        Solution updated = solutionRepository.save(s);
        return solutionMapper.toSolutionDto(updated);
    }

    public void deleteSolution(long id) {
        if (solutionRepository.existsById(id)) {
            solutionRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Solution " + id + " not found");
        }
    }
}
