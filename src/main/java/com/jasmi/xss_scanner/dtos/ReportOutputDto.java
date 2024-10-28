package com.jasmi.xss_scanner.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReportOutputDto {
    public long id;
    private String url;
    private LocalDateTime date;
    private String screenshotUrl;
    private List<VulnerabilityOutputDto> vulnerabilities;

}

