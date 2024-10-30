package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestOutputDto;
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

    public ScanRequestService(ScanRequestRepository scanRequestRepository, ScanRequestMapper scanRequestMapper, VulnerabilityRepository vulnerabilityRepository, ScanResultRepository scanResultRepository) {
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
        ScanResult scanResult = performScanAndCreateResult(scanRequest);
        scanRequest.setScanResult(scanResult);

        ScanRequest savedScanRequest = scanRequestRepository.save(scanRequest);

        return scanRequestMapper.toScanRequestDto(savedScanRequest);
    }

    private ScanResult performScanAndCreateResult(ScanRequest scanRequest) {

        List<Vulnerability> detectedVulnerabilities = scanUrl((scanRequest.getUrl()));

        ScanResult scanResult = new ScanResult();
        scanResult.setScanRequest(scanRequest);
        scanResult.setVulnerabilities(detectedVulnerabilities);

        return scanResult;
    }

    private List<Vulnerability> scanUrl(String url) {
        List<Vulnerability> detectedVulnerabilities = new ArrayList<>();

        try{
            String pageHtml = getPageHtml(url);
            if (pageHtml != null) {
                List<Vulnerability> allVulnerabilities = vulnerabilityRepository.findAll();
                detectedVulnerabilities = findVulnerabilities(pageHtml, allVulnerabilities);
                logResults(url, detectedVulnerabilities);
            }
        } catch (IOException e) {
            System.out.println("error fetching URL: " + e.getMessage());
        }

        return detectedVulnerabilities;
    }

    private String getPageHtml(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.html();
    }

    private List<Vulnerability> findVulnerabilities(String pageHtml, List<Vulnerability> allVulnerabilities){
        List<Vulnerability> detectedVulnerabilities = new ArrayList<>();

        for (Vulnerability vulnerability : allVulnerabilities) {
            if (checkVulnerability(pageHtml, vulnerability.getCode())) {
                detectedVulnerabilities.add(vulnerability);
            }
        }

        return detectedVulnerabilities;
    }

    private boolean checkVulnerability(String pageHtml, String vulnerableCodePattern){
        Pattern pattern = Pattern.compile(vulnerableCodePattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pageHtml);

        return matcher.find();
    }

    private void logResults(String url, List<Vulnerability> detectedVulnerabilities){
        if (detectedVulnerabilities.isEmpty()) {
            System.out.println("No vulnerabilities found for URL: " + url);
        } else {
            System.out.println("Detected vulnerabilities for URL: " + url);
            for (Vulnerability v : detectedVulnerabilities) {
                System.out.println("- " + v.getName());
            }
        }
    }

    public void deleteScanRequest(long id) {
        if (scanRequestRepository.existsById(id)) {
            scanRequestRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Scan request " + id + " not found");
        }
    }

    public ScanRequestOutputDto addScreenshotToScanRequest(byte[] screenshot, String fileName, String fileType, Long id) {
        ScanRequest scanRequest = scanRequestRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException("ScanRequest not found for id "+id));

        scanRequest.setScreenshot(screenshot);
        scanRequest.setScreenshotFilename(fileName);
        scanRequest.setScreenshotFileType(fileType);

        scanRequestRepository.save(scanRequest);

        return scanRequestMapper.toScanRequestDto(scanRequest);
    }

    public byte[] getScreenshotById(Long id) {
        Optional<ScanRequest> optionalScanRequest = scanRequestRepository.findById(id);
        if(optionalScanRequest.isEmpty()){
            throw new RecordNotFoundException("Scanrequest "+id+" not found");
        }
        return optionalScanRequest.get().getScreenshot();
    }
}
