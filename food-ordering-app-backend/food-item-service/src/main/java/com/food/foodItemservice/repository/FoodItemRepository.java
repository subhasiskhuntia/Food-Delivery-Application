package com.food.foodItemservice.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.foodItemservice.entity.FoodItem;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {

	List<FoodItem> findAllByRestaurantId(Long restaurantId);
	List<FoodItem> findAllByRestaurantIdIn(List<Long> ids);

}
