package com.food.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.userservice.model.User;


public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByPassword(String password);

}
