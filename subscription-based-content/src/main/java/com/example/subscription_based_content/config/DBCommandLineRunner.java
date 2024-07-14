package com.example.subscription_based_content.config;

import com.example.subscription_based_content.dao.entity.SubscriptionType;
import com.example.subscription_based_content.dao.repository.SubscriptionTypeRepository;
import com.example.subscription_based_content.util.SubscriptionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DBCommandLineRunner implements CommandLineRunner {

    private final SubscriptionTypeRepository subscriptionTypeRepository;


    @Override
    public void run(String... args) throws Exception {
        SubscriptionType basic = SubscriptionType.builder()
                .type(SubscriptionTypeEnum.BASIC.getName())
                .monthlyCost(5.00)
                .build();

        subscriptionTypeRepository.save(basic);

        SubscriptionType premium = SubscriptionType.builder()
                .type(SubscriptionTypeEnum.PREMIUM.getName())
                .monthlyCost(10.00)
                .build();

        subscriptionTypeRepository.save(premium);

        SubscriptionType premiumPlus = SubscriptionType.builder()
                .type(SubscriptionTypeEnum.PREMIUM_PLUS.getName())
                .monthlyCost(15.00)
                .build();

        subscriptionTypeRepository.save(premiumPlus);
    }
}
