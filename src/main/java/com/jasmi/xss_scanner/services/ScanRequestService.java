package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScanRequestService {
    private final ScanRequestRepository scanRequestRepository;

    public ScanRequestService(ScanRequestRepository scanRequestRepository) {
        this.scanRequestRepository = scanRequestRepository;
    }

    public List<ScanRequestOutputDto> getAllScanRequests(){
        return scanRequestRepository.findAll()
                .stream()
                .map((scanRequest)-> toScanRequestDto(scanRequest))
                .collect(Collectors.toList());
    }

    public ScanRequestOutputDto getScanRequestById(long id){
        Optional<ScanRequest> optionalScanRequest = scanRequestRepository.findById(id);
        if(optionalScanRequest.isPresent()){
            ScanRequest scanrequest = optionalScanRequest.get();
            return toScanRequestDto(scanrequest);
        }
        else{
            throw new RecordNotFoundException(id+" not found");
        }
    }

    public ScanRequestOutputDto saveScanRequest(ScanRequestInputDto scanRequest) {
        ScanRequest sr = toScanRequest(scanRequest);
        ScanRequest savedScanRequest = scanRequestRepository.save(sr);
        return toScanRequestDto(savedScanRequest);

    }

    //DTO
    public static ScanRequestOutputDto toScanRequestDto(ScanRequest scanRequest){
        var dto = new ScanRequestOutputDto();

        dto.setId(scanRequest.getId());
        dto.setUrl(scanRequest.getUrl());
        dto.setRequestTimestamp(scanRequest.getRequestTimestamp());
        dto.setUrl(scanRequest.getUrl());

        return dto;
    }

    public ScanRequest toScanRequest(ScanRequestInputDto dto){
        var scanRequest = new ScanRequest();

        scanRequest.setUrl(dto.getUrl());
        scanRequest.setRequestTimestamp(dto.getRequestTimestamp());
        scanRequest.setImage(dto.getImage());

        return scanRequest;
    }

}
