package com.jasmi.xss_scanner.dtos;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScanRequestOutputDto {
    public long id;
    private String url;
    private LocalDateTime requestTimestamp;
    private byte Image;
}
