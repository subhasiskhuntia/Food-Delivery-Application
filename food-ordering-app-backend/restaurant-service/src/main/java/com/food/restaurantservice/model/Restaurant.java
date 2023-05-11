package com.food.restaurantservice.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uniqueUserId",columnNames = {"user_id"})
})
public class Restaurant {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;
	    private String name;
	    private String mobileNumber;
	    private String description;
	    private boolean approvalStatus;
	    @Lob
	    private String image;
	    @Column(name = "user_id")
	    private long userId;
	    @OneToOne(cascade = CascadeType.ALL)
	    private RestaurantAddress address;
	    private LocalTime openedOn;
	    private LocalTime closedOn;
	    @CreationTimestamp
	    private LocalDateTime createdOn;
	    @UpdateTimestamp
	    private LocalDateTime updatedOn;
}
