package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanRequestMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ScanRequestService {
    private final ScanRequestRepository scanRequestRepository;
    private final ScanRequestMapper scanRequestMapper;
    private final VulnerabilityRepository vulnerabilityRepository;
    private final ScanResultRepository scanResultRepository;

    public ScanRequestService(ScanRequestRepository scanRequestRepository, ScanRequestMapper scanRequestMapper, VulnerabilityRepository vulnerabilityRepository, ScanResultRepository scanResultRepository) {
        this.scanRequestRepository = scanRequestRepository;
        this.scanRequestMapper = scanRequestMapper;
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.scanResultRepository = scanResultRepository;
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

        List<Vulnerability> detectedVulnerabilities = scanUrl((scanRequest.getUrl()));

        ScanResult scanResult = new ScanResult();
        scanResult.setScanRequest(scanRequest);
        scanResult.setVulnerabilities(detectedVulnerabilities);

        scanRequest.setScanResult(scanResult);

        ScanRequest savedScanRequest = scanRequestRepository.save(scanRequest);
        return scanRequestMapper.toScanRequestDto(savedScanRequest);
    }

    private List<Vulnerability> scanUrl(String url) {
        List<Vulnerability> detectedVulnerabilities = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            String pageHtml = doc.html();

//            System.out.println("Scanned HTML: " + pageHtml); // test1

            List<Vulnerability> allVulnerabilities = vulnerabilityRepository.findAll();

//            System.out.println("Available vulnerabilities: " + allVulnerabilities.size()); // test2

            for (Vulnerability vulnerability : allVulnerabilities) {
                String vulnerableCodePattern = vulnerability.getCode();

//                System.out.println("Checking for vulnerability pattern: " + vulnerableCodePattern); // test3

                Pattern pattern = Pattern.compile(vulnerableCodePattern, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(pageHtml);

                if (matcher.find()) {
                    detectedVulnerabilities.add(vulnerability);
//                    System.out.println("Detected vulnerability: " + vulnerableCodePattern); // test4
                } else {
//                    System.out.println("Pattern not found: " + vulnerableCodePattern); // test5
                }
            }

            if (detectedVulnerabilities.isEmpty()) {
                System.out.println("No vulnerabilities found for URL: " + url);
            } else {
                System.out.println("Detected vulnerabilities for URL: " + url);
                for (Vulnerability v : detectedVulnerabilities) {
                    System.out.println("- " + v.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching URL: " + e.getMessage());
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

    public ScanRequestOutputDto assignScreenshotToScanRequest(byte[] screenshotData, Long id) {
        ScanRequest scanRequest = scanRequestRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("ScanRequest not found for id "+id));

        scanRequest.setScreenshot(screenshotData);

        scanRequestRepository.save(scanRequest);

        return scanRequestMapper.toScanRequestDto(scanRequest);
    }
}
