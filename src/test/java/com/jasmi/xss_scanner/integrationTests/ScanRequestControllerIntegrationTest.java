package com.jasmi.xss_scanner.integrationTests;

import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.repositories.ScanRequestRepository;
import com.jasmi.xss_scanner.repositories.ScanResultRepository;
import com.jasmi.xss_scanner.repositories.UserRepository;
import com.jasmi.xss_scanner.repositories.VulnerabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "testuser", roles = "INTERNALUSER")
public class ScanRequestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ScanRequestRepository scanRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScanResultRepository scanResultRepository;

    @Autowired
    private VulnerabilityRepository vulnerabilityRepository;

    @BeforeEach
    public void setUp() {

        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");
        user = userRepository.save(user);

        ScanRequest scanRequest = new ScanRequest();
        scanRequest.setUrl("http://integrationtest.nl");
        scanRequest.setRequestTimestamp(LocalDateTime.now());
        scanRequest.setScreenshotFilename("Screenshot 2024-05-23 164247.png");
        scanRequest.setScreenshotFileType("image/png");
        scanRequest.setUser(user);

        scanRequest = scanRequestRepository.save(scanRequest);

        ScanResult scanResult = new ScanResult();
        scanResult.setScanRequest(scanRequest);

        Vulnerability vulnerability1 = new Vulnerability();
        vulnerability1.setDescription("SQL Injection");
        vulnerability1 = vulnerabilityRepository.save(vulnerability1);

        Vulnerability vulnerability2 = new Vulnerability();
        vulnerability2.setDescription("XSS");
        vulnerability2 = vulnerabilityRepository.save(vulnerability2);

        scanResult.getVulnerabilities().add(vulnerability1);
        scanResult.getVulnerabilities().add(vulnerability2);

        scanResultRepository.save(scanResult);
    }

    @Test
    void shouldGetScanRequests() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/xss_scanner_api/scan_requests")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].url").value("http://integrationtest.nl"))
                .andExpect(jsonPath("$[0].requestTimestamp").exists())
                .andExpect(jsonPath("$[0].screenshotFileName").exists())
                .andExpect(jsonPath("$[0].screenshotFileType").exists())
                .andExpect(jsonPath("$[0].scanResult").isNumber())
                .andExpect(jsonPath("$[0].userId").isNumber())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

    }
}
