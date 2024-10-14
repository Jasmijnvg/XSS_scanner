package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.ScanResultOutputDto;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.services.ScanRequestService;
import org.springframework.stereotype.Component;

@Component
public class ScanResultMapper {
    private final ScanRequestMapper scanRequestMapper;

    public ScanResultMapper(ScanRequestMapper scanRequestMapper) {
        this.scanRequestMapper = scanRequestMapper;
    }

    public ScanResultOutputDto toScanResultDto(ScanResult scanResult) {
        var dto = new ScanResultOutputDto();
        dto.setId(scanResult.getId());
        if(scanResult.getScanRequest()!=null){
            dto.setScanRequest(scanRequestMapper.toScanRequestDto(scanResult.getScanRequest()));
        }
        return dto;
    }

    public ScanResult toScanResult(ScanResultInputDto dto) {
        var scanResult = new ScanResult();
        return scanResult;
    }
}
