package com.food.restaurantservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.restaurantservice.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    public Restaurant findRestaurantByUserId(Long id);
    @Query("select restaurant from Restaurant restaurant where trim(lower(restaurant.address.city)) like concat('%',trim(lower(:city)),'%')")
    List<Restaurant> findRestaurantsByACityName(String city);
}
