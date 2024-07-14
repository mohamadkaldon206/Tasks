package com.example.subscription_based_content.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "SUBSCRIPTION_TYPE")
public class SubscriptionType {

    @Id
    private String type;

    @Column(nullable = false)
    private double fees;

}
