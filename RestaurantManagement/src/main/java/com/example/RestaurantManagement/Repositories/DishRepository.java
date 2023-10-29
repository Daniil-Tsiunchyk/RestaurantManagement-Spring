package com.example.RestaurantManagement.Repositories;

import com.example.RestaurantManagement.Models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {}
