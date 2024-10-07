package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanRequestMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import org.springframework.stereotype.Service;

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

    public List<ScanRequestOutputDto> getAllScanRequests(){
        return scanRequestRepository.findAll()
                .stream()
                .map((scanRequest)-> scanRequestMapper.toScanRequestDto(scanRequest))
                .collect(Collectors.toList());
    }

    public ScanRequestOutputDto getScanRequestById(long id){
        Optional<ScanRequest> optionalScanRequest = scanRequestRepository.findById(id);
        if(optionalScanRequest.isPresent()){
            ScanRequest scanrequest = optionalScanRequest.get();
            return scanRequestMapper.toScanRequestDto(scanrequest);
        }
        else{
            throw new RecordNotFoundException(id+" not found");
        }
    }

    public ScanRequestOutputDto saveScanRequest(ScanRequestInputDto scanRequest) {
        ScanRequest sr = scanRequestMapper.toScanRequest(scanRequest);
        ScanRequest savedScanRequest = scanRequestRepository.save(sr);
        return scanRequestMapper.toScanRequestDto(savedScanRequest);

    }

}
