package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        if (orders == null) {
            orders = new ArrayList<>();
        }
        model.addAttribute("orders", orders);
        return "orders";
    }


    @PostMapping("/orders/delete")
    public String deleteOrder(@RequestParam("id") int id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
