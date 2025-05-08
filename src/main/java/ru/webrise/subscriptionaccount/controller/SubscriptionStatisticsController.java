package ru.webrise.subscriptionaccount.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionResponse;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionTopResponse;
import ru.webrise.subscriptionaccount.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/subscriptions/top")
@RequiredArgsConstructor
public class SubscriptionStatisticsController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionTopResponse>> getTopSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getTopSubscriptions());
    }
}
