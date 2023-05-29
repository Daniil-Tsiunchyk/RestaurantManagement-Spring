package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.*;
import com.example.RestaurantManagement.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
        Tables table = tablesRepository.findById(orderRequest.getTable_id())
                .orElseThrow(() -> new RuntimeException("Стол не найден: " + orderRequest.getTable_id()));

        newOrder.setTable(table);
        newOrder.setStartTime(new Timestamp(System.currentTimeMillis()));
        newOrder.setStatus("Принят");
        newOrder.setInformation("Информация о блюдах формируется...");

        newOrder = orderRepository.save(newOrder);

        Map<String, Pair<Integer, Double>> dishInfo = new HashMap<>();
        double totalCost = 0.0;

        if (orderRequest.getDish_ids() != null) {
            for (Integer dishId : orderRequest.getDish_ids()) {
                Dish dish = dishRepository.findById(dishId)
                        .orElseThrow(() -> new RuntimeException("Блюдо не найдено: " + dishId));

                int count = orderRequest.getDish_counts().getOrDefault(dishId, 0);

                for (int i = 0; i < count; i++) {
                    OrderedDish orderedDish = new OrderedDish();
                    orderedDish.setDish(dish);
                    orderedDish.setOrder(newOrder);
                    orderedDish.setStatus("Принят");
                    orderedDishRepository.save(orderedDish);

                    totalCost += dish.getCost();
                }

                String dishName = dish.getName();
                int quantity = count;
                dishInfo.put(dishName, Pair.of(quantity, dish.getCost()));
            }
        }

        StringBuilder information = new StringBuilder("\n=========== Заказ ===========\n");
        for (Map.Entry<String, Pair<Integer, Double>> entry : dishInfo.entrySet()) {
            information.append(entry.getKey())
                    .append(":\n- количество: ")
                    .append(entry.getValue().getFirst())
                    .append("\n- цена за единицу: ")
                    .append(entry.getValue().getSecond())
                    .append("\n-----------------------------\n");
        }
        information = new StringBuilder(information.toString().replace("\n", "<br>"));

        newOrder.setInformation(information.toString());
        newOrder.setTotalCost(totalCost);
        orderRepository.save(newOrder);

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
