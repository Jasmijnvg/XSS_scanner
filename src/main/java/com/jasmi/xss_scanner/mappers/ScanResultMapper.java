package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.ScanResultOutputDto;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.services.ScanRequestService;
import org.springframework.stereotype.Component;

@Component
public class ScanResultMapper {

    public ScanResultOutputDto toScanResultDto(ScanResult scanResult) {
        var dto = new ScanResultOutputDto();
        dto.setId(scanResult.getId());
        if(scanResult.getScanRequest()!=null){
            dto.setScanRequest(ScanRequestMapper.toScanRequestDto(scanResult.getScanRequest()));
        }
        return dto;
    }

    public ScanResult toScanResult(ScanResultInputDto dto) {
        var scanResult = new ScanResult();
        return scanResult;
    }
}
