package com.food.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.food.orderservice.model.UserOrder;

import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<UserOrder,Long> {
	List<UserOrder> findUserOrdersByUserId(Long userId);
    @Query(value =  "select order_item.id as id, "
    		+ "food_item.id as foodItemId, "
    		+ "user_order.transaction_id as transaction_id, "
    		+ "order_item.quantity,restaurant.id as restaurantId, "
    		+ "food_item.price as price  ,"
    		+ "user.full_name as user_full_name, "
    		+ "concat(user_address.house_number ,' , ',user_address.locality,' , ',user_address.city,' , ',user_address.state,' , ',user_address.pincode) as user_address_detail, "
    		+ "user_order.order_status as order_status, "
    		+ "user_order.ordered_on as ordered_on, "
//    		+ "restaurant.image as restaurant_image, "
    		+ "restaurant.name as restaurantName from order_item "
    		+ "inner join food_item on food_item.id=order_item.food_item_id  "
    		+ "inner join restaurant on food_item.restaurant_id=restaurant.id  "
    		+ "inner join user_order on order_item.order_id=user_order.id  "
    		+ "inner join user on user.id=user_order.user_id  "
    		+ "inner join user_address on user_address.id=user_order.user_address_id  "
    		+ "inner join restaurant_address on restaurant_address.restaurant_id=restaurant.id "
    		+ "where restaurant.id=:restaurantId "
    		+ "order by user_order.ordered_on desc ",nativeQuery = true)
    public List<Map<String, Object>> findOrdersOfARestaurants(@Param("restaurantId") Long restaurantId);
    @Query(value =  "select order_item.id as id, "
    		+ "food_item.id as foodItemId, "
    		+ "user_order.transaction_id as transaction_id, "
    		+ "order_item.quantity,restaurant.id as restaurantId, "
    		+ "food_item.price as price  ,"
    		+ "user.full_name as user_full_name, "
    		+ "concat(user_address.house_number ,' , ',user_address.locality,' , ',user_address.city,' , ',user_address.state,' , ',user_address.pincode) as user_address_detail, "
    		+ "user_order.order_status as order_status, "
    		+ "user_order.ordered_on as ordered_on, "
//    		+ "restaurant.image as restaurant_image, "
    		+ "restaurant.name as restaurantName from order_item "
    		+ "inner join food_item on food_item.id=order_item.food_item_id  "
    		+ "inner join restaurant on food_item.restaurant_id=restaurant.id  "
    		+ "inner join user_order on order_item.order_id=user_order.id  "
    		+ "inner join user on user.id=user_order.user_id  "
    		+ "inner join user_address on user_address.id=user_order.user_address_id  "
    		+ "inner join restaurant_address on restaurant_address.restaurant_id=restaurant.id "
    		+ "where user.id=:userId "
    		+ "order by user_order.ordered_on desc ",nativeQuery = true)
    public List<Map<String, Object>> findOrdersOfAUser(@Param("userId") Long userId);
    @Query("select userOrder from UserOrder userOrder where userOrder.restaurantId=:restaurantId")
    public List<UserOrder> findUserOrderByRestaurantId(@Param("restaurantId") Long restaurantId);
    @Query("select userOrder from UserOrder userOrder where userOrder.userId=:userId")
    public List<UserOrder> findUserOrderByUserId(@Param("userId") Long userId);
}

