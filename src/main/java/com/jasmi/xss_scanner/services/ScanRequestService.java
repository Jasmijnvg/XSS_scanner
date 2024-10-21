package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanRequestMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScanRequestService {
    private final ScanRequestRepository scanRequestRepository;
    private final ScanRequestMapper scanRequestMapper;
    private final VulnerabilityRepository vulnerabilityRepository;

    public ScanRequestService(ScanRequestRepository scanRequestRepository, ScanRequestMapper scanRequestMapper, VulnerabilityRepository vulnerabilityRepository) {
        this.scanRequestRepository = scanRequestRepository;
        this.scanRequestMapper = scanRequestMapper;
        this.vulnerabilityRepository = vulnerabilityRepository;
    }

    public List<ScanRequestOutputDto> getAllScanRequests() {
        return scanRequestRepository.findAll()
                .stream()
                .map(scanRequest -> scanRequestMapper.toScanRequestDto(scanRequest))
                .collect(Collectors.toList());
    }

    public ScanRequestOutputDto getScanRequestById(long id) {
        Optional<ScanRequest> optionalScanRequest = scanRequestRepository.findById(id);
        if (optionalScanRequest.isPresent()) {
            ScanRequest scanRequest = optionalScanRequest.get();
            return scanRequestMapper.toScanRequestDto(scanRequest);
        } else {
            throw new RecordNotFoundException("Scan request " + id + " not found");
        }
    }

    public ScanRequestOutputDto saveScanRequest(ScanRequestInputDto scanRequestInputDto) {
        ScanRequest scanRequest = scanRequestMapper.toScanRequest(scanRequestInputDto);
        ScanResult scanResult = new ScanResult();
        scanResult.setScanRequest(scanRequest);
        scanRequest.setScanResult(scanResult);

        try {
            List<Vulnerability> detectedVulnerabilities = scanUrl(scanRequest.getUrl());
            scanResult.setVulnerabilities(detectedVulnerabilities);
        } catch (IOException e){
            throw new RuntimeException("Failed to scan the URL: "+scanRequest.getUrl(), e);
        }

        ScanRequest savedScanRequest = scanRequestRepository.save(scanRequest);
        return scanRequestMapper.toScanRequestDto(savedScanRequest);
    }

    private List<Vulnerability> scanUrl(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();

        List<Vulnerability> allVulnerabilities = vulnerabilityRepository.findAll();
        List<Vulnerability> detectedVulnerabilities = new ArrayList<>();

        for (Vulnerability vulnerability : allVulnerabilities){
            if(doc.body().text().contains(vulnerability.getCode())){
                detectedVulnerabilities.add(vulnerability);
            }
        }
        return detectedVulnerabilities;
    }

    public void deleteScanRequest(long id) {
        if (scanRequestRepository.existsById(id)) {
            scanRequestRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Scan request " + id + " not found");
        }
    }

//    public ScanResult scanUrl(String url) throws IOException {
//        Document doc = Jsoup.connect(url).get();
//        Set<Vulnerability> allVulnerabilities = new HashSet<>(vulnerabilityRepository.findAll());
//        Set<Vulnerability> detectedVulnerabilities = new HashSet<>();
//
//        for (Vulnerability vulnerability : allVulnerabilities) {
//            if (doc.body().text().contains(vulnerability.getCode())) {
//                detectedVulnerabilities.add(vulnerability);
//            }
//        }
}
