package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestOutputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.ScanRequestMapper;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import com.jasmi.xss_scanner.repositories.UserRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import com.jasmi.xss_scanner.security.ApiUserDetails;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;
    private final UserService userService;

    public ScanRequestService(ScanRequestRepository scanRequestRepository, ScanRequestMapper scanRequestMapper, VulnerabilityRepository vulnerabilityRepository, ScanResultRepository scanResultRepository, UserRepository userRepository, UserService userService) {
        this.scanRequestRepository = scanRequestRepository;
        this.scanRequestMapper = scanRequestMapper;
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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

        addUserToScanRequest(scanRequest);

        ScanRequest savedScanRequest = scanRequestRepository.save(scanRequest);

        return scanRequestMapper.toScanRequestDto(savedScanRequest);
    }

    private void addUserToScanRequest(ScanRequest scanRequest) {
        Object username = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (username instanceof UserDetails) {
            String actualUsername = ((UserDetails) username).getUsername();

            User user = userRepository.findByUserName(actualUsername)
                    .orElseThrow(() -> new RecordNotFoundException("User with username " + actualUsername + " not found"));

            scanRequest.setUser(user);
            user.getScanRequests().add(scanRequest);
            scanRequestRepository.save(scanRequest);
        } else {
            throw new IllegalStateException("No authenticated user found");
        }
    }




//    private void addUserToScanRequest(ScanRequest scanRequest) {
//        // Haal de geauthenticeerde gebruiker op
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (userDetails instanceof ApiUserDetails) {
//            ApiUserDetails apiUserDetails = (ApiUserDetails) userDetails;
//
//            // Gebruik de ID van de geauthenticeerde gebruiker
//            Long userId = apiUserDetails.getId();
//
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RecordNotFoundException("User " + userId + " not found"));
//
//            scanRequest.setUser(user);
//            user.getScanRequests().add(scanRequest); // Optioneel, indien je dit wilt bijhouden
//            userRepository.save(user); // Opslaan van de gebruiker kan noodzakelijk zijn als er wijzigingen zijn
//        } else {
//            throw new IllegalStateException("No authenticated user found");
//        }
//    }

//    private void addUserToScanRequest(ScanRequest scanRequest, ScanRequestInputDto scanRequestInputDto) {
//        Long userId = scanRequestInputDto.getUserId();
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RecordNotFoundException("User "+userId+" not found"));
//
//        user.getScanRequests().add(scanRequest);
//        scanRequest.setUser(user);
//
//        userRepository.save(user);
//    }


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
