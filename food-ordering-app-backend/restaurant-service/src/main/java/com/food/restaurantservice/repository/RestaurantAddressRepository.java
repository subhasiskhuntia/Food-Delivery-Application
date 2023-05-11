package com.food.restaurantservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.restaurantservice.model.RestaurantAddress;

@Repository
public interface RestaurantAddressRepository extends JpaRepository<RestaurantAddress,Long> {
    @Query(value =
            "select restaurant_id from restaurant_address address where (select ST_Distance_Sphere(point(address.latitude, address.longitude),point(:latitude, :longitude))<20)",
            nativeQuery = true)
    List<Long> getNearByRestaurants(String latitude, String longitude);
}