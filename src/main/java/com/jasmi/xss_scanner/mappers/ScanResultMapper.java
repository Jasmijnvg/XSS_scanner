package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.ScanResultOutputDto;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.services.ScanRequestService;
import com.jasmi.xss_scanner.services.VulnerabilityService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ScanResultMapper {
    private final ScanRequestMapper scanRequestMapper;
    private final VulnerabilityMapper vulnerabilityMapper;

    public ScanResultMapper(ScanRequestMapper scanRequestMapper, VulnerabilityMapper vulnerabilityMapper) {
        this.scanRequestMapper = scanRequestMapper;
        this.vulnerabilityMapper = vulnerabilityMapper;
    }

    public ScanResultOutputDto toScanResultDto(ScanResult scanResult) {
        var dto = new ScanResultOutputDto();

        dto.setId(scanResult.getId());
        if(scanResult.getScanRequest()!=null){
            dto.setScannedUrl(scanResult.getScanRequest().getUrl());
            dto.setScanDate(scanResult.getScanRequest().getRequestTimestamp());
        }
        if(scanResult.getVulnerabilities()!=null){
            dto.setVulnerabilities(
                    scanResult.getVulnerabilities()
                            .stream()
                            .map(vulnerabilityMapper::toVulnerabilityDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public ScanResult toScanResult(ScanResultInputDto dto) {
        var scanResult = new ScanResult();
        return scanResult;
    }
}
