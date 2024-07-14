package com.example.subscription_based_content.dao.repository;

import com.example.subscription_based_content.dao.entity.Subscription;
import com.example.subscription_based_content.dto.response.UserSubscriptionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserIdAndSubscriptionTypeType(Long userId, String subscription);

    Optional<Subscription> findByUserIdAndStatus(Long userId, String status);

    @Query("""
            select new com.example.subscription_based_content.dto.response.UserSubscriptionResponse(
            s.user.id, s.subscriptionType.type, cast(s.status as String)
            ) from Subscription s where s.status = 'ACTIVE'
""")
    List<UserSubscriptionResponse> findActiveStatus();
}
