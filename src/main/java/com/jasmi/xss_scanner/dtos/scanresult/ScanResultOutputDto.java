package com.jasmi.xss_scanner.dtos.scanresult;

import com.jasmi.xss_scanner.dtos.vulnerability.VulnerabilityOutputDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ScanResultOutputDto {
    private Long id;

    private String scannedUrl;
    private LocalDateTime scanDate;
    private String screenshotUrl;
    private String userName;
    private List<VulnerabilityOutputDto> vulnerabilities;

}
