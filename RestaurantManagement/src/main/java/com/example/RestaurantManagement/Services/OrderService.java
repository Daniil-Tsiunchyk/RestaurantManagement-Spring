package com.example.RestaurantManagement.Services;

import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Models.OrderedDish;
import com.example.RestaurantManagement.Models.Tables;
import com.example.RestaurantManagement.Repositories.OrderRepository;
import com.example.RestaurantManagement.Repositories.OrderedDishRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderedDishRepository orderedDishRepository;

    public OrderService(OrderRepository orderRepository, OrderedDishRepository orderedDishRepository) {
        this.orderRepository = orderRepository;
        this.orderedDishRepository = orderedDishRepository;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order == null) {
                order = new Order();
            }
            if (order.getTable() == null) {
                order.setTable(new Tables());
            }
        }
        return orders;
    }

    @Transactional
    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        List<OrderedDish> orderedDishes = orderedDishRepository.findByOrder(order);
        orderedDishRepository.deleteInBatch(orderedDishes);
        orderRepository.delete(order);
    }
}
