package com.example.paymentprocessor.controller;

import com.example.paymentprocessor.dto.PaymentOutcomeDto;
import com.example.paymentprocessor.service.PaymentProcessingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final PaymentProcessingService paymentProcessingService;

    public ReportController(PaymentProcessingService paymentProcessingService) {
        this.paymentProcessingService = paymentProcessingService;
    }

    @GetMapping("/summary")
    public Page<PaymentOutcomeDto> summary(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return paymentProcessingService.getActivity(null, null, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "processedAt")));
    }

    @GetMapping("/activity")
    public Page<PaymentOutcomeDto> activity(@RequestParam(required = false) String status,
                                            @RequestParam(required = false) String accountId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return paymentProcessingService.getActivity(status, accountId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "processedAt")));
    }
}
