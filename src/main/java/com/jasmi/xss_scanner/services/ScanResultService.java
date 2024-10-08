package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.ScanResultOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanResultMapper;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScanResultService {
    private final ScanResultRepository scanResultRepository;
    private final ScanResultMapper scanResultMapper;

    public ScanResultService(ScanResultRepository scanResultRepository, ScanResultMapper scanResultMapper) {
        this.scanResultRepository = scanResultRepository;
        this.scanResultMapper = scanResultMapper;
    }

    public List<ScanResultOutputDto> getAllScanResults() {
        return scanResultRepository.findAll()
                .stream()
                .map(scanResultMapper::toScanResultDto)
                .collect(Collectors.toList());
    }

    public ScanResultOutputDto getScanResultById(Integer id) {
        Optional<ScanResult> scanResult = scanResultRepository.findById(id);
        if (scanResult.isPresent()) {
            return scanResultMapper.toScanResultDto(scanResult.get());
        } else {
            throw new RecordNotFoundException("ScanResult " + id + " not found");
        }
    }

    public ScanResultOutputDto saveScanResult(ScanResultInputDto scanResultDto) {
        ScanResult scanResult = scanResultMapper.toScanResult(scanResultDto);
        ScanResult savedScanResult = scanResultRepository.save(scanResult);
        return scanResultMapper.toScanResultDto(savedScanResult);
    }

    public ScanResultOutputDto updateScanResult(Integer id, ScanResultInputDto scanResultDto) {
        if (!scanResultRepository.existsById(id)) {
            throw new RecordNotFoundException("ScanResult " + id + " not found");
        }
        ScanResult scanResult = scanResultMapper.toScanResult(scanResultDto);
        scanResult.setId(id);  // Ensure the ID is set
        ScanResult updatedScanResult = scanResultRepository.save(scanResult);
        return scanResultMapper.toScanResultDto(updatedScanResult);
    }

    public void deleteScanResult(Integer id) {
        if (scanResultRepository.existsById(id)) {
            scanResultRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("ScanResult " + id + " not found");
        }
    }
}
