package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Staff;
import com.example.RestaurantManagement.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthorisationController {

    @Autowired
    StaffRepository staffRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam String login, @RequestParam String password, Model model) {
        Optional<Staff> optionalStaff = staffRepository.findStaffByLogin(login);

        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            if (staff.getPassword().equals(password) && staff.getDismissalFromWork() == null) {
                switch (staff.getRole()) {
                    case "ОФИЦИАНТ" -> {
                        return "redirect:/create-order";
                    }
                    case "АДМИНИСТРАТОР" -> {
                        return "redirect:/staff";
                    }
                    case "МЕНЕДЖЕР" -> {
                        return "redirect:/view-schedule";
                    }
                    case "ПОВАР" -> {
                        return "redirect:/kitchen";
                    }
                    default -> {
                        model.addAttribute("errorMessage", "Неверная роль");
                        return "login";
                    }
                }
            }
        }

        model.addAttribute("errorMessage", "Неверный логин или пароль");
        return "login";
    }
}
