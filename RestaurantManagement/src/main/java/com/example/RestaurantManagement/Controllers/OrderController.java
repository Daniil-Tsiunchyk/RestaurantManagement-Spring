package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Dish;
import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Models.OrderRequest;
import com.example.RestaurantManagement.Models.OrderedDish;
import com.example.RestaurantManagement.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderedDishRepository orderedDishRepository;

    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private DishTypeRepository dishTypeRepository;

    @GetMapping("/create-order")
    public String createOrderForm(Model model) {
        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("types", dishTypeRepository.findAll());
        model.addAttribute("tables", tablesRepository.findAll());
        return "create-order";
    }


    @PostMapping("/create-order")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        System.out.println(orderRequest);
        Order newOrder = new Order();

        newOrder.setInformation("Заказ корректно обработан");
        newOrder.setTable(orderRequest.getTable());
        newOrder.setStartTime(new Timestamp(System.currentTimeMillis()));
        newOrder.setStatus("Принят");

        newOrder = orderRepository.save(newOrder);

        List<OrderedDish> orderedDishes = new ArrayList<>();

        if(orderRequest.getDish_ids() != null) {
            for (Integer dishId : orderRequest.getDish_ids()) {
                Dish dish = dishRepository.findById(dishId)
                        .orElseThrow(() -> new RuntimeException("Dish not found: " + dishId));

                OrderedDish orderedDish = new OrderedDish();
                orderedDish.setDish(dish);
                orderedDish.setOrder(newOrder);
                orderedDish.setStatus("Принят");
                orderedDishRepository.save(orderedDish);
                orderedDishes.add(orderedDish);
            }
        }
        return "redirect:/view-orders";
    }



    @GetMapping("/view-orders")
    public String viewOrders(@RequestParam(required = false) String status,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model) {
        List<Order> orders = orderRepository.findAll();

        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        if (date != null) {
            orders = orders.stream()
                    .filter(order -> order.getStartTime().toLocalDateTime().toLocalDate().equals(date))
                    .collect(Collectors.toList());
        }

        model.addAttribute("orders", orders);
        return "view-orders";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable int id, @RequestParam String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            order.setStatus(status);
            order.getOrderedDishes().forEach(dish -> dish.setStatus(status));

            if (status.equals("Закрыт")) {
                order.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
            }

            orderRepository.save(order);
        }

        return "redirect:/view-orders";
    }

    @GetMapping("/manage-orders")
    public String viewOrdersManager(@RequestParam(required = false) String status,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    Model model) {
        List<Order> orders = orderRepository.findAll();

        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        if (date != null) {
            orders = orders.stream()
                    .filter(order -> order.getStartTime().toLocalDateTime().toLocalDate().equals(date))
                    .collect(Collectors.toList());
        }

        model.addAttribute("orders", orders);
        return "manage-orders";
    }

    @PostMapping("/update-status-manager/{id}")
    public String updateStatusManager(@PathVariable int id, @RequestParam String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            order.setStatus(status);
            order.getOrderedDishes().forEach(dish -> dish.setStatus(status));

            if (status.equals("Закрыт")) {
                order.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
            }

            orderRepository.save(order);
        }

        return "redirect:/manage-orders";
    }

    @PostMapping("/update-dish-status-manager/{id}")
    public String updateDishStatus(@PathVariable int id, @RequestParam String status) {
        Optional<OrderedDish> optionalDish = orderedDishRepository.findById(id);

        if (optionalDish.isPresent()) {
            OrderedDish dish = optionalDish.get();

            dish.setStatus(status);
            orderedDishRepository.save(dish);

            Order order = dish.getOrder();
            boolean allSameStatus = order.getOrderedDishes().stream()
                    .allMatch(d -> d.getStatus().equals(status));

            if (allSameStatus) {
                order.setStatus(status);
                if (status.equals("Закрыт")) {
                    order.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
                }
                orderRepository.save(order);
            }
        }

        return "redirect:/manage-orders";
    }

}
