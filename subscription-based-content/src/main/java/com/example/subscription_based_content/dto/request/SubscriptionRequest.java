package com.example.subscription_based_content.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {

    private Long userId;

    private String subscriptionType;
}
