package com.jasmi.xss_scanner.controllers;


import com.jasmi.xss_scanner.services.VulnerabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api/vulnerabilities")
public class VulnerabiltyController {

    private final VulnerabilityService vulnerabilityService;

    public VulnerabiltyController(VulnerabilityService vulnerabilityService) {
        this.vulnerabilityService = vulnerabilityService;
    }

    @GetMapping()
    public ResponseEntity<List<VulnerabilityOutputDto>> getVulnerabilities() {}
}
