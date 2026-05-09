package com.example.paymentprocessor.controller;

import com.example.paymentprocessor.dto.PaymentOutcomeDto;
import com.example.paymentprocessor.service.PaymentProcessingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final PaymentProcessingService paymentProcessingService;

    public AccountController(PaymentProcessingService paymentProcessingService) {
        this.paymentProcessingService = paymentProcessingService;
    }

    @GetMapping("/{accountId}/history")
    public Page<PaymentOutcomeDto> history(@PathVariable String accountId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return paymentProcessingService.getAccountHistory(accountId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "processedAt")));
    }
}
