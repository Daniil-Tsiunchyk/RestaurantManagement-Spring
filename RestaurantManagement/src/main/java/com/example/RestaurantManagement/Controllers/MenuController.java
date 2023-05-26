package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Dish;
import com.example.RestaurantManagement.Models.DishType;
import com.example.RestaurantManagement.Repositories.DishTypeRepository;
import com.example.RestaurantManagement.Services.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MenuController {

    private final DishService dishService;
    private final DishTypeRepository dishTypeRepository;

    public MenuController(DishService dishService, DishTypeRepository dishTypeRepository) {
        this.dishService = dishService;
        this.dishTypeRepository = dishTypeRepository;
    }

    @GetMapping("/menu")
    public String showMenu(Model model) {
        List<Dish> dishes = dishService.getAllDishes();
        List<DishType> dishTypes = dishTypeRepository.findAll();
        model.addAttribute("dishes", dishes);
        model.addAttribute("dishTypes", dishTypes);
        return "menu";
    }

    @PostMapping("/menu/edit")
    public String editDish(@RequestParam("id") int id, @RequestParam("name") String name,
                           @RequestParam("cost") double cost, @RequestParam("type") String typeName) {
        if (dishService.checkIfDishIsOrdered(id)) {
        } else {
            dishService.editDish(id, name, cost, typeName);
        }
        return "redirect:/menu";
    }

    @PostMapping("/menu/delete")
    public String deleteDish(@RequestParam("id") int id) {
        if (dishService.checkIfDishIsOrdered(id)) {
        } else {
            dishService.deleteDish(id);
        }
        return "redirect:/menu";
    }

    @PostMapping("/menu/add")
    public String addDish(@RequestParam("name") String name, @RequestParam("cost") double cost,
                          @RequestParam("type") String typeName) {
        dishService.addDish(name, cost, typeName);
        return "redirect:/menu";
    }
}
