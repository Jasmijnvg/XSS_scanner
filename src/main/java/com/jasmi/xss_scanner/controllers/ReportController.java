package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.ReportOutputDto;
import com.jasmi.xss_scanner.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("xss_scanner_api")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String showReportForm() {
        return "report";
    }

    @GetMapping("/report/{scanResultId}")
    public String generateReport(@PathVariable Long scanResultId, Model model) {
        ReportOutputDto report = reportService.generateReport(scanResultId);
        model.addAttribute("report", report);
        return "report";
    }
}
