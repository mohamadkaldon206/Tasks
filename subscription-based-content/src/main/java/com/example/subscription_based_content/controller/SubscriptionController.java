package com.example.subscription_based_content.controller;

import com.example.subscription_based_content.dto.request.SubscriptionChangeRequest;
import com.example.subscription_based_content.dto.request.SubscriptionRequest;
import com.example.subscription_based_content.dto.response.BillingResponse;
import com.example.subscription_based_content.dto.response.SubscriptionResponse;
import com.example.subscription_based_content.dto.response.UserSubscriptionList;
import com.example.subscription_based_content.dto.response.UserSubscriptionResponse;
import com.example.subscription_based_content.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest request) {

        return ResponseEntity.ok(service.createSubscription(request));
    }

    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<UserSubscriptionResponse> getSubscription(@PathVariable Long userId) {

        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @DeleteMapping("/subscriptions/{userId}")
    public ResponseEntity<UserSubscriptionResponse> cancelSubscription(@PathVariable Long userId) {

        return ResponseEntity.ok(service.cancelSubscription(userId));
    }


    @GetMapping("/subscriptions/active")
    public ResponseEntity<UserSubscriptionList> getActiveSubscriptions() {

        return ResponseEntity.ok(service.getActiveSubscriptions());

    }

    @PutMapping("/subscriptions/{userId}/upgrade")
    public ResponseEntity<UserSubscriptionResponse> upgradeSubscription(@PathVariable Long userId, @RequestBody SubscriptionChangeRequest request) {

        return ResponseEntity.ok(service.upgradeSubscription(userId, request));
    }

    @GetMapping("/subscriptions/billing/{month}")
    public ResponseEntity<BillingResponse> getBillingReport(@PathVariable String month) {

        return ResponseEntity.ok(service.findByMonth(month));
    }
}
