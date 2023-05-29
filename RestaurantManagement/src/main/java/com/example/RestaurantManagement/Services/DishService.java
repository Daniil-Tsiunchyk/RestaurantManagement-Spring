package com.example.RestaurantManagement.Services;

import com.example.RestaurantManagement.Models.Dish;
import com.example.RestaurantManagement.Models.DishType;
import com.example.RestaurantManagement.Models.OrderedDish;
import com.example.RestaurantManagement.Repositories.DishRepository;
import com.example.RestaurantManagement.Repositories.DishTypeRepository;
import com.example.RestaurantManagement.Repositories.OrderedDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final OrderedDishRepository orderedDishRepository;
    private final DishTypeRepository dishTypeRepository;

    @Autowired
    public DishService(DishRepository dishRepository, OrderedDishRepository orderedDishRepository, DishTypeRepository dishTypeRepository) {
        this.dishRepository = dishRepository;
        this.orderedDishRepository = orderedDishRepository;
        this.dishTypeRepository = dishTypeRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public boolean checkIfDishIsOrdered(int id) {
        List<OrderedDish> orderedDishes = orderedDishRepository.findAllByDish_Id(id);
        return !orderedDishes.isEmpty();
    }

    public void editDish(int id, String name, double cost, String typeName) {
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isPresent()) {
            Dish dish = optionalDish.get();
            dish.setName(name);
            dish.setCost(cost);
            DishType dishType = dishTypeRepository.findByName(typeName);
            if (dishType != null) {
                dish.setType(dishType);
            }
            dishRepository.save(dish);
        }
    }

    public void deleteDish(int id) {
        dishRepository.deleteById(id);
    }

    public void addDish(String name, double cost, String typeName) {
        DishType dishType = dishTypeRepository.findByName(typeName);
        if (dishType != null) {
            Dish dish = new Dish();
            dish.setName(name);
            dish.setCost(cost);
            dish.setType(dishType);
            dishRepository.save(dish);
        }
    }

    public Dish getDishById(int id) {
        return dishRepository.findById(id).orElse(null);
    }

    public void editDishDetails(int id, String name, String description, String recipe){//}, byte[] image) {
        Dish dish = getDishById(id);
        if (dish != null) {
            dish.setName(name);
            dish.setDescription(description);
            dish.setRecipe(recipe);
           // dish.setImage(image);
            dishRepository.save(dish);
        }
    }
}
