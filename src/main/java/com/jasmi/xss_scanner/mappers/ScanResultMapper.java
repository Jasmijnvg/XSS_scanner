package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.scanresult.ScanResultOutputDto;
import com.jasmi.xss_scanner.models.ScanResult;
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
            if(scanResult.getScanRequest().getUser() != null){
                dto.setUserName((scanResult.getScanRequest().getUser().getUserName()));
            }
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
}
