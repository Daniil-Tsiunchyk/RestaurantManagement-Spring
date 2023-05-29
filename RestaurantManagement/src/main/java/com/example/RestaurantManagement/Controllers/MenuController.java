package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Dish;
import com.example.RestaurantManagement.Models.DishType;
import com.example.RestaurantManagement.Repositories.DishTypeRepository;
import com.example.RestaurantManagement.Services.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/menu/{id}/details")
    public String showDishDetails(@PathVariable("id") int id, Model model) {
        Dish dish = dishService.getDishById(id);
        model.addAttribute("dish", dish);
        return "dishDetails";
    }

    @PostMapping("/menu/{id}/edit")
    public String editDishDetails(@PathVariable("id") int id, @RequestParam("name") String name,
                                  @RequestParam("description") String description,
                                  @RequestParam("recipe") String recipe){
//                                  @RequestParam("image") MultipartFile image) throws IOException {
        dishService.editDishDetails(id, name, description, recipe);//, image.getBytes());
        return "redirect:/menu/{id}/details";
    }
    @GetMapping("kitchen/{id}/recipe")
    public String showRecipe(@PathVariable("id") int id, Model model) {
        Dish dish = dishService.getDishById(id);
        model.addAttribute("dish", dish);
        return "recipe";
    }
}
