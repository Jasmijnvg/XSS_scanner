package com.jasmi.xss_scanner.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScanRequestInputDto {
    @NotNull(message = "url may not be empty")
    private String url;
    private LocalDateTime requestTimestamp;
    private byte[] screenshot;
}
