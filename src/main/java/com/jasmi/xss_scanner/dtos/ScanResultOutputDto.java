package com.jasmi.xss_scanner.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanResultOutputDto {
    private Long id;

    public ScanRequestOutputDto scanRequest;
}
