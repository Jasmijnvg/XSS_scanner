package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.ReportOutputDto;
import com.jasmi.xss_scanner.models.ScanResult;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReportMapper {

    private final VulnerabilityMapper vulnerabilityMapper;

    public ReportMapper(VulnerabilityMapper vulnerabilityMapper) {
        this.vulnerabilityMapper = vulnerabilityMapper;
    }

    public ReportOutputDto toReportDto(ScanResult scanResult) {
        ReportOutputDto reportDto = new ReportOutputDto();

        reportDto.setId(scanResult.getId());
        reportDto.setUrl(scanResult.getScanRequest().getUrl());
        reportDto.setDate(scanResult.getScanRequest().getRequestTimestamp());
        reportDto.setScreenshotUrl(scanResult.getScanRequest().getScreenshotFilename());

        if (scanResult.getVulnerabilities() != null) {
            reportDto.setVulnerabilities(scanResult.getVulnerabilities()
                    .stream()
                    .map(vulnerabilityMapper::toVulnerabilityDto)
                    .collect(Collectors.toList()));
        }

        return reportDto;
    }
}
