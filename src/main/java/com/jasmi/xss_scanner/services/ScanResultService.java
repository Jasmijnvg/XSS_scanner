package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.scanresult.ScanResultOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanResultMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import com.jasmi.xss_scanner.security.ApiUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanResultService {
    private final ScanResultRepository scanResultRepository;
    private final ScanResultMapper scanResultMapper;
    private final ScanRequestRepository scanRequestRepository;

    public ScanResultService(ScanResultRepository scanResultRepository, ScanResultMapper scanResultMapper, ScanRequestRepository scanRequestRepository, VulnerabilityRepository vulnerabilityRepository) {
        this.scanResultRepository = scanResultRepository;
        this.scanResultMapper = scanResultMapper;
        this.scanRequestRepository = scanRequestRepository;
    }

    public List<ScanResultOutputDto> getAllScanResults() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new SecurityException("User is not authenticated");
        }

        if (isUserRole(authentication, "ROLE_USER")) {
            return getScanResultsForUser();
        } else {
            return getAllScanResultsForAdmins();
        }
    }

    private boolean isUserRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals(role));
    }

    private List<ScanResultOutputDto> getScanResultsForUser() {
        String username = getCurrentUser();
        return scanResultRepository.findByScanRequestUserName(username)
                .stream()
                .map(scanResultMapper::toScanResultDto)
                .peek(this::addScreenshotUrlToScanResult)
                .collect(Collectors.toList());
    }

    private List<ScanResultOutputDto> getAllScanResultsForAdmins() {
        return scanResultRepository.findAll()
                .stream()
                .map(scanResultMapper::toScanResultDto)
                .peek(this::addScreenshotUrlToScanResult)
                .collect(Collectors.toList());
    }

    public ScanResultOutputDto getScanResultById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = getCurrentUser();
        String currentRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        List<ScanResult> scanResults = getAccessibleScanResults(username, currentRole);

        return scanResults.stream()
                .filter(scanResult -> scanResult.getId().equals(id))
                .findFirst()
                .map(scanResult -> {
                    ScanResultOutputDto dto = scanResultMapper.toScanResultDto(scanResult);
                    addScreenshotUrlToScanResult(dto);
                    return dto;
                })
                .orElseThrow(() -> new RecordNotFoundException("Scan result not found or this user does not have access"));
    }

    private List<ScanResult> getAccessibleScanResults(String username, String role) {
        if ("ROLE_USER".equals(role)) {
            return scanResultRepository.findByScanRequestUserName(username);
        } else {
            return scanResultRepository.findAll();
        }
    }

    public void deleteScanResult(Long id) {
        if (scanResultRepository.existsById(id)) {
            scanResultRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("ScanResult " + id + " not found");
        }
    }

    private void addScreenshotUrlToScanResult(ScanResultOutputDto dto) {
        ScanRequest scanRequest = scanRequestRepository.findById(dto.getId())
                .orElseThrow(() -> new RecordNotFoundException("Scan request "+dto.getId()+" not found"));

        if (hasScreenshot(scanRequest.getId())) {
            String screenshotUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/xss_scanner_api/scan_request/" + scanRequest.getId() + "/screenshot")
                    .toUriString();
            dto.setScreenshotUrl(screenshotUrl);
        }
        else {
            dto.setScreenshotUrl("No screenshot uploaded");
        }
    }

    private boolean hasScreenshot(Long scanRequestId) {
        return scanRequestRepository.findById(scanRequestId)
                .map(scanRequest -> scanRequest.getScreenshot() != null && scanRequest.getScreenshot().length > 0)
                .orElse(false);
    }

    private String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((ApiUserDetails) principal).getUsername();
        }
        return null;
    }
}
