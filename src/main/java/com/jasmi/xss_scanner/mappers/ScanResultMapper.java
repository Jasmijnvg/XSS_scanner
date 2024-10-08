package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.ScanResultOutputDto;
import com.jasmi.xss_scanner.models.ScanResult;
import org.springframework.stereotype.Component;

@Component
public class ScanResultMapper {

    public ScanResultOutputDto toScanResultDto(ScanResult scanResult) {
        var dto = new ScanResultOutputDto();
        dto.setId(scanResult.getId());
        // Map other fields if they exist
        return dto;
    }

    public ScanResult toScanResult(ScanResultInputDto dto) {
        var scanResult = new ScanResult();
        // Set the necessary fields from dto to scanResult
        return scanResult;
    }
}
