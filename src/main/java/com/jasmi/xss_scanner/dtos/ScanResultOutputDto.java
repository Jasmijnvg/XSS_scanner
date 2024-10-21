package com.jasmi.xss_scanner.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScanResultOutputDto {
    private Long id;

    public ScanRequestOutputDto scanRequest;
    private List<VulnerabilityOutputDto> vulnerabilities;

}
