package com.food.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.userservice.model.User;
import com.food.userservice.model.UserLog;

public interface UserLogRepository extends JpaRepository<UserLog, Long>{
	  Optional<UserLog> findUserLogByUser(User user);
}
