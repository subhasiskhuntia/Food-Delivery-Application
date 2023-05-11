package com.food.orderservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uniqueTransactionId",columnNames = {"transaction_Id"})
})
@Entity
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price;
    private Long userId;
    private Long userAddressId;
    @Column(name = "transaction_Id",updatable = false)
    private String transactionId;
    private Long restaurantId;
    @Column(nullable = false,columnDefinition = "varchar(20) default 'ordered'")
    private String orderStatus="ordered";
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderedOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderItem> orderItems;
}

