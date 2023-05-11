package com.food.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.food.cartservice.model.Cart;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findCartByUserId(Long id);
    
    @Modifying
    @Transactional
    @Query("delete from Cart cart where cart.userId=:userId")
    void deleteByUserId(long userId);
}


