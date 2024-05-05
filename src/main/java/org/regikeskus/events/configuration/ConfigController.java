package org.regikeskus.events.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/config")
public class ConfigController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping("/payment-methods")
    public ResponseEntity<List<String>> getPaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethods());
    }
}
