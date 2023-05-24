package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Dish;
import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Models.OrderedDish;
import com.example.RestaurantManagement.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDate;
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
    public String submitOrder(@RequestParam Integer table_id,
                              @RequestParam List<Integer> dish_ids,
                              RedirectAttributes redirectAttributes) {

        if (dish_ids == null || dish_ids.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Выберите хотя бы одно блюдо перед созданием заказа");
            return "redirect:/create-order";
        }

        Order order = new Order();
        order.setTotalCost(calculateTotalCost(dish_ids));
        order.setTable(tablesRepository.findById(table_id).orElseThrow());
        order.setStartTime(new Timestamp(System.currentTimeMillis()));
        order.setStatus("Принят");
        order = orderRepository.save(order);
        for (Integer dish_id : dish_ids) {
            Optional<Dish> dishOptional = dishRepository.findById(dish_id);
            if (dishOptional.isPresent()) {
                OrderedDish orderedDish = new OrderedDish();
                orderedDish.setDish(dishOptional.get());
                orderedDish.setOrder(order);
                orderedDish.setStatus("Принят");
                orderedDishRepository.save(orderedDish);
            }
        }

        redirectAttributes.addFlashAttribute("message", "Заказ успешно создан!");
        return "redirect:/view-orders";
    }


    private double calculateTotalCost(List<Integer> dish_ids) {
        double totalCost = 0;
        for (Integer dish_id : dish_ids) {
            Optional<Dish> dishOptional = dishRepository.findById(dish_id);
            if (dishOptional.isPresent()) {
                totalCost += dishOptional.get().getCost();
            }
        }
        return totalCost;
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

            orderRepository.save(order);
        }

        return "redirect:/view-orders";
    }


}
