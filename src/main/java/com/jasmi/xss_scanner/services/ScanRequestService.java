package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanRequestMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScanRequestService {
    private final ScanRequestRepository scanRequestRepository;
    private final ScanRequestMapper scanRequestMapper;

    public ScanRequestService(ScanRequestRepository scanRequestRepository, ScanRequestMapper scanRequestMapper) {
        this.scanRequestRepository = scanRequestRepository;
        this.scanRequestMapper = scanRequestMapper;
    }

    public List<ScanRequestOutputDto> getAllScanRequests() {
        return scanRequestRepository.findAll()
                .stream()
                .map(scanRequest -> scanRequestMapper.toScanRequestDto(scanRequest))
                .collect(Collectors.toList());
    }

    public ScanRequestOutputDto getScanRequestById(long id) {
        Optional<ScanRequest> optionalScanRequest = scanRequestRepository.findById(id);
        if (optionalScanRequest.isPresent()) {
            ScanRequest scanRequest = optionalScanRequest.get();
            return scanRequestMapper.toScanRequestDto(scanRequest);
        } else {
            throw new RecordNotFoundException("Scan request " + id + " not found");
        }
    }

    public ScanRequestOutputDto saveScanRequest(ScanRequestInputDto scanRequestInputDto) {
        ScanRequest scanRequest = scanRequestMapper.toScanRequest(scanRequestInputDto);

        ScanResult scanResult = new ScanResult();
        scanRequest.setScanResult(scanResult);

        ScanRequest savedScanRequest = scanRequestRepository.save(scanRequest);
        return scanRequestMapper.toScanRequestDto(savedScanRequest);
    }

    public void deleteScanRequest(long id) {
        if (scanRequestRepository.existsById(id)) {
            scanRequestRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Scan request " + id + " not found");
        }
    }
}
