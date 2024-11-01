package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.scanresult.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.scanresult.ScanResultOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanResultMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScanResultService {
    private final ScanResultRepository scanResultRepository;
    private final ScanResultMapper scanResultMapper;
    private final ScanRequestRepository scanRequestRepository;

    public ScanResultService(ScanResultRepository scanResultRepository, ScanResultMapper scanResultMapper, ScanRequestRepository scanRequestRepository, VulnerabilityRepository vulnerabilityRepository) {
        this.scanResultRepository = scanResultRepository;
        this.scanResultMapper = scanResultMapper;
        this.scanRequestRepository = scanRequestRepository;
    }

    public List<ScanResultOutputDto> getAllScanResults() {
        return scanResultRepository.findAll()
                .stream()
                .map(scanResultMapper::toScanResultDto)
                .peek(this::addScreenshotUrlToScanResult)
                .collect(Collectors.toList());
    }

    public ScanResultOutputDto getScanResultById(Long id) {
        Optional<ScanResult> scanResult = scanResultRepository.findById(id);
        if (scanResult.isPresent()) {
            ScanResultOutputDto dto = scanResultMapper.toScanResultDto(scanResult.get());
            addScreenshotUrlToScanResult(dto);
            return dto;
        } else {
            throw new RecordNotFoundException("ScanResult " + id + " not found");
        }
    }

//    public ScanResultOutputDto updateScanResult(Long id, ScanResultInputDto scanResultDto) {
//        if (!scanResultRepository.existsById(id)) {
//            throw new RecordNotFoundException("ScanResult " + id + " not found");
//        }
//        ScanResult scanResult = scanResultMapper.toScanResult(scanResultDto);
//        scanResult.setId(id);
//        ScanResult updatedScanResult = scanResultRepository.save(scanResult);
//        return scanResultMapper.toScanResultDto(updatedScanResult);
//    }

    public void deleteScanResult(Long id) {
        if (scanResultRepository.existsById(id)) {
            scanResultRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("ScanResult " + id + " not found");
        }
    }

    private void addScreenshotUrlToScanResult(ScanResultOutputDto dto) {
        ScanRequest scanRequest = scanRequestRepository.findById(dto.getId())
                .orElseThrow(() -> new RecordNotFoundException("Scan request "+dto.getId()+" not found"));

        if (hasScreenshot(scanRequest.getId())) {
            String screenshotUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/xss_scanner_api/scan_request/" + scanRequest.getId() + "/screenshot")
                    .toUriString();
            dto.setScreenshotUrl(screenshotUrl);
        }
        else {
            dto.setScreenshotUrl("No screenshot uploaded");
        }
    }

    private boolean hasScreenshot(Long scanRequestId) {
        return scanRequestRepository.findById(scanRequestId)
                .map(scanRequest -> scanRequest.getScreenshot() != null && scanRequest.getScreenshot().length > 0)
                .orElse(false);
    }

}
