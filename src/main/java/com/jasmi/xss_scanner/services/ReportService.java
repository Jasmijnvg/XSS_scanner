package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ReportOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ReportMapper;
import com.jasmi.xss_scanner.mappers.VulnerabilityMapper;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ReportService {
    private final ScanResultRepository scanResultRepository;
    private final ReportMapper reportMapper;
    private final VulnerabilityMapper vulnerabilityMapper;

    public ReportService(ScanResultRepository scanResultRepository, ReportMapper reportMapper, VulnerabilityMapper vulnerabilityMapper) {
        this.scanResultRepository = scanResultRepository;
        this.reportMapper = reportMapper;
        this.vulnerabilityMapper = vulnerabilityMapper;
    }

    public ReportOutputDto generateReport(Long scanResultId) {
        ScanResult scanResult = scanResultRepository.findById(scanResultId)
                .orElseThrow(() -> new RecordNotFoundException("ScanResult "+scanResultId+" not found"));

        ReportOutputDto report = new ReportOutputDto();
        report.setUrl(scanResult.getScanRequest().getUrl());
        report.setDate(scanResult.getScanRequest().getRequestTimestamp());
        report.setScreenshotUrl("test");
        report.setVulnerabilities(scanResult.getVulnerabilities()
                .stream()
                .map(vulnerabilityMapper::toVulnerabilityDto)
                .collect(Collectors.toList()));

        return report;
    }
}
