package com.food.foodItemservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.foodItemservice.entity.FoodItemImage;

@Repository
public interface FoodItemImageRepository extends JpaRepository<FoodItemImage,Long> {
	
}