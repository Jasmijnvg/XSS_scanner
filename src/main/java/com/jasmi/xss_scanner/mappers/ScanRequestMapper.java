package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.models.ScanRequest;
import org.springframework.stereotype.Component;

@Component
public class ScanRequestMapper {
    public ScanRequestOutputDto toScanRequestDto(ScanRequest scanRequest){
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

