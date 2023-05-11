package com.food.foodItemservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.foodItemservice.entity.Food;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	
	Optional<Food> findFoodByFoodNameAndMealType(String foodName, String mealType);

	@Query("select food from Food food where trim(lower(food.foodName) ) like concat('%',:foodName,'%') ")
	Optional<Food> findFoodByFoodName(String foodName);

	@Query("select food.foodName from Food  food")
	List<String> findDistinctFoodName();

	List<Food> findFoodByMealType(String mealType);

	List<Food> findFoodByCuisine(String cuisine);
}
