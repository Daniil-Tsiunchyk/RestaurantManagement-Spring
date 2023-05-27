package com.example.RestaurantManagement.Services;

import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Models.Tables;
import com.example.RestaurantManagement.Repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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

    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}
