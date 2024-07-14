package com.example.subscription_based_content.dao.repository;

import com.example.subscription_based_content.dao.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    List<Billing> findAllByBillingMonth(String billingMonth);

}
