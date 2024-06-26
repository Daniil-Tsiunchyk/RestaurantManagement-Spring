package com.example.RestaurantManagement.Repositories;

import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Models.OrderedDish;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedDishRepository
  extends JpaRepository<OrderedDish, Integer> {
  List<OrderedDish> findAllByOrder(Order order);

  List<OrderedDish> findAllByDish_Id(int id);

  List<OrderedDish> findByOrder(Order order);
}
