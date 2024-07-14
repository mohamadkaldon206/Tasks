package com.example.subscription_based_content.util;

public enum SubscriptionTypeEnum {

    BASIC("5.00"), PREMIUM("10.00"), PREMIUM_PLUS("15.00");

    private final String subscriptionType;

    SubscriptionTypeEnum(String name) {
        this.subscriptionType = name;
    }

    public String getName() {
        return subscriptionType;
    }

}
