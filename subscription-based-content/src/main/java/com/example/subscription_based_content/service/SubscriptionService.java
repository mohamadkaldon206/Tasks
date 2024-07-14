package com.example.subscription_based_content.service;

import com.example.subscription_based_content.dao.entity.Billing;
import com.example.subscription_based_content.dao.entity.Subscription;
import com.example.subscription_based_content.dao.entity.SubscriptionType;
import com.example.subscription_based_content.dao.repository.BillingRepository;
import com.example.subscription_based_content.dao.repository.SubscriptionRepository;
import com.example.subscription_based_content.dao.repository.SubscriptionTypeRepository;
import com.example.subscription_based_content.dao.repository.UserRepository;
import com.example.subscription_based_content.dto.request.SubscriptionChangeRequest;
import com.example.subscription_based_content.dto.request.SubscriptionRequest;
import com.example.subscription_based_content.dto.response.*;
import com.example.subscription_based_content.util.SubscriptionStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private BillingRepository billingRepository;

    public SubscriptionResponse createSubscription(SubscriptionRequest request) {

        Optional<Subscription> findSubscription = subscriptionRepository.findByUserIdAndSubscriptionTypeType(request.getUserId(), request.getSubscriptionType());

        if (findSubscription.isPresent()) {
            throw new IllegalArgumentException("Subscription Exists");
        } else {

            var user = userRepository.findById(request.getUserId());

            if (user.isPresent()) {

                SubscriptionType subscriptionType = subscriptionTypeRepository.findById(request.getSubscriptionType()).orElseThrow(() -> new IllegalArgumentException("No Subscription Type found"));

                Subscription subscription = Subscription.builder()
                        .user(user.get())
                        .subscriptionType(subscriptionType)
                        .startDate(LocalDateTime.now())
                        .status(SubscriptionStatus.ACTIVE)
                        .build();
                Subscription savedSubscription = subscriptionRepository.save(subscription);

                Billing billing = Billing.builder()
                        .user(user.get())
                        .subscription(subscription)
                        .billingMonth(LocalDate.now().getMonth().toString())
                        .fees(subscriptionType.getFees())
                        .build();

                billingRepository.save(billing);

                return SubscriptionResponse.builder()
                        .subscriptionId(savedSubscription.getId())
                        .status(savedSubscription.getStatus().name())
                        .initialBillAmount(subscriptionType.getFees())
                        .build();

            }
        }

        return null;
    }

    public UserSubscriptionResponse findByUserId(Long userId) {

        Optional<Subscription> findSubscription = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE.name());

        if (findSubscription.isPresent()) {
            Subscription subscription = findSubscription.get();
            return UserSubscriptionResponse.builder()
                    .userId(subscription.getUser().getId())
                    .status(subscription.getStatus().name())
                    .subscriptionType(subscription.getSubscriptionType().getType())
                    .build();
        }
        return null;
    }

    public UserSubscriptionResponse cancelSubscription(Long userId) {

        Optional<Subscription> findSubscription = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE.name());

        if (findSubscription.isPresent()) {
            Subscription subscription = findSubscription.get();

            subscription.setEndDate(LocalDateTime.now());
            subscription.setStatus(SubscriptionStatus.CANCELED);

            Subscription saveSubscription = subscriptionRepository.save(subscription);

            UserSubscriptionResponse.builder()
                    .userId(saveSubscription.getUser().getId())
                    .status(saveSubscription.getStatus().name())
                    .build();
        }
        return null;
    }

    public UserSubscriptionList getActiveSubscriptions() {
        List<UserSubscriptionResponse> activeSubscriptions = subscriptionRepository.findActiveStatus();
        return UserSubscriptionList.builder().activeSubscriptions(activeSubscriptions).build();
    }

    public UserSubscriptionResponse upgradeSubscription(Long userId, SubscriptionChangeRequest request) {

        Optional<Subscription> findSubscription = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE.name());

        if (findSubscription.isPresent()) {

            Subscription subscription = findSubscription.get();
            Optional<SubscriptionType> subscriptionType = subscriptionTypeRepository.findById(request.getNewSubscriptionType());
            subscriptionType.ifPresent(subscription::setSubscriptionType);
            Subscription saved = subscriptionRepository.save(subscription);

            return UserSubscriptionResponse.builder()
                    .userId(subscription.getUser().getId())
                    .subscriptionType(saved.getSubscriptionType().getType())
                    .status(saved.getStatus().name())
                    .build();

        }
        return null;
    }

    public BillingResponse findByMonth(String month) {

        List<Billing> billingRecords = billingRepository.findAllByBillingMonth(month);
        Map<Subscription, Double> report = billingRecords.stream()
                .collect(Collectors.groupingBy(
                        Billing::getSubscription,
                        Collectors.summingDouble(Billing::getFees)
                ));

        List<BillingReport> result = new ArrayList<>();

        for (Map.Entry<Subscription, Double> entry : report.entrySet()) {

            BillingReport billingReport = BillingReport.builder()
                    .totalUsers(0)      // TODO Need to Check
                    .amount(entry.getValue())
                    .subscriptionType(entry.getKey().getSubscriptionType().getType())
                    .build();

            result.add(billingReport);
        }

        return BillingResponse.builder().billingReport(result).build();

    }
}
