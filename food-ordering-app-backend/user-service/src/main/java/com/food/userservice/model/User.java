package com.food.userservice.model;

import java.time.LocalDateTime;
import javax.persistence.UniqueConstraint;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "uniqueUsername", columnNames = { "username" }),
		@UniqueConstraint(name = "uniqueEmail", columnNames = { "email" })

})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "email")
	private String email;
	@Column(name = "username")
	private String username;
	private String fullName;
	private String mobileNumber;
	private String password;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserAddress> userAddress;
	@Column(columnDefinition = "varchar(30) default 'user'")
	private String role = "user";
}
