package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestOutputDto;
import com.jasmi.xss_scanner.models.ScanRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ScanRequestMapper {

    public ScanRequestOutputDto toScanRequestDto(ScanRequest scanRequest){
        var dto = new ScanRequestOutputDto();

        dto.setId(scanRequest.getId());
        dto.setUrl(scanRequest.getUrl());
        dto.setRequestTimestamp(scanRequest.getRequestTimestamp());
        dto.setScreenshotFileName(scanRequest.getScreenshotFilename());
        dto.setScreenshotFileType(scanRequest.getScreenshotFileType());
        dto.setScanResult(scanRequest.getScanResult().getId());
        if (scanRequest.getUser() != null) {
            dto.setUserId(scanRequest.getUser().getId());
        }

        return dto;
    }
    
    public ScanRequest toScanRequest(ScanRequestInputDto dto){
        var scanRequest = new ScanRequest();

        scanRequest.setUrl(dto.getUrl());
        scanRequest.setRequestTimestamp(dto.getRequestTimestamp());
        scanRequest.setScreenshotFilename(dto.getScreenshotFileName());
        scanRequest.setScreenshotFileType(dto.getScreenshotFileType());

        return scanRequest;
    }

}

