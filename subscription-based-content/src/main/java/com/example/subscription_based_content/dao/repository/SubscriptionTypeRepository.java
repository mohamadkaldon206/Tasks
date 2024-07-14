package com.example.subscription_based_content.dao.repository;

import com.example.subscription_based_content.dao.entity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, String> {
}
