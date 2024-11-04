package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ScanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(columnDefinition="varchar(500)")
    private String url;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime requestTimestamp;

    @Lob
    private byte[] screenshot;
    private String screenshotFilename;
    private String screenshotFileType;

    @OneToOne(mappedBy="scanRequest", cascade=CascadeType.ALL)
    private ScanResult scanResult;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
