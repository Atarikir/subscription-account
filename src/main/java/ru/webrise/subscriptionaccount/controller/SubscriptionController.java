package ru.webrise.subscriptionaccount.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.webrise.subscriptionaccount.dto.request.SubscriptionRequest;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionResponse;
import ru.webrise.subscriptionaccount.service.SubscriptionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{id}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @PostMapping
  public ResponseEntity<SubscriptionResponse> createSubscription(
      @Valid @RequestBody SubscriptionRequest request, @PathVariable UUID id) {
    return ResponseEntity.ok(subscriptionService.createSubscription(id, request));
  }

  @GetMapping
  public ResponseEntity<List<SubscriptionResponse>> getSubscriptions(@PathVariable UUID id) {
    return ResponseEntity.ok(subscriptionService.getUserSubscriptions(id));
  }

  @DeleteMapping("/{sub_id}")
  public ResponseEntity<?> deleteSubscription(@PathVariable UUID id, @PathVariable UUID sub_id) {
    subscriptionService.deleteSubscription(id, sub_id);
    return ResponseEntity.ok().build();
  }
}
