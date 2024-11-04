package com.jasmi.xss_scanner.dtos.scanrequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScanRequestInputDto {
    @NotNull(message = "url may not be empty")
    @Size(max = 500, message = "URL can not contain over 500 characters")
    private String url;
    private LocalDateTime requestTimestamp;
    private byte[] screenshot;
    private String screenshotFileName;
    private String screenshotFileType;
    private long userId;
}
