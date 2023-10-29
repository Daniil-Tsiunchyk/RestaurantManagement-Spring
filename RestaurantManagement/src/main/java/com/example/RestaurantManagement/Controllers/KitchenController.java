package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Order;
import com.example.RestaurantManagement.Models.OrderedDish;
import com.example.RestaurantManagement.Repositories.OrderRepository;
import com.example.RestaurantManagement.Repositories.OrderedDishRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KitchenController {

  @Autowired
  private OrderedDishRepository orderedDishRepository;

  @Autowired
  private OrderRepository orderRepository;

  @GetMapping("/kitchen")
  public String getKitchen(Model model) {
    List<OrderedDish> orderedDishes = orderedDishRepository.findAll();
    List<OrderedDish> acceptedDishes = orderedDishes
      .stream()
      .filter(dish -> dish.getStatus().equals("Принят"))
      .collect(Collectors.toList());
    model.addAttribute("orderedDishes", acceptedDishes);
    return "kitchen";
  }

  @PostMapping("/update-dish-status/{id}")
  public String updateDishStatus(
    @PathVariable("id") int id,
    @RequestParam("status") String status
  ) {
    Optional<OrderedDish> optionalOrderedDish = orderedDishRepository.findById(
      id
    );
    if (optionalOrderedDish.isPresent()) {
      OrderedDish orderedDish = optionalOrderedDish.get();
      orderedDish.setStatus(status);
      orderedDishRepository.save(orderedDish);

      List<OrderedDish> dishesInOrder = orderedDishRepository.findAllByOrder(
        orderedDish.getOrder()
      );

      boolean allMatch = dishesInOrder
        .stream()
        .allMatch(dish -> dish.getStatus().equals(status));

      if (allMatch) {
        Order order = orderedDish.getOrder();
        order.setStatus(status);
        orderRepository.save(order);
      }
    }
    return "redirect:/kitchen";
  }
}
