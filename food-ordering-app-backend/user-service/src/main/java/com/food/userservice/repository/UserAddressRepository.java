package com.food.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.userservice.dto.UserAddressDto;
import com.food.userservice.model.User;
import com.food.userservice.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long>{
    List<UserAddress> findByUser(User user);
}
