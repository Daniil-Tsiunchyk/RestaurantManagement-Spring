package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Staff;
import com.example.RestaurantManagement.Models.WorkHour;
import com.example.RestaurantManagement.Repositories.StaffRepository;
import com.example.RestaurantManagement.Repositories.WorkHourRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ShiftController {
    private final StaffRepository staffRepository;
    private final WorkHourRepository workHourRepository;

    public ShiftController(StaffRepository staffRepository, WorkHourRepository workHourRepository) {
        this.staffRepository = staffRepository;
        this.workHourRepository = workHourRepository;
    }

    @GetMapping("/assign-shifts")
    public String assignShifts(Model model) {
        model.addAttribute("staffs", staffRepository.findAll());
        return "assign-shifts";
    }

    @PostMapping("/assign-shift")
    public String assignShift(int staff_id, Date date) {
        Staff staff = staffRepository.findById(staff_id).orElseThrow();
        WorkHour workHour = new WorkHour();
        workHour.setStaff(staff);
        workHour.setDate(date);
        workHourRepository.save(workHour);
        return "redirect:/assign-shifts";
    }

    @GetMapping("/view-schedule")
    public String viewSchedule(Model model) {
        Map<Staff, Long> shiftStats = workHourRepository.findAll().stream()
                .collect(Collectors.groupingBy(WorkHour::getStaff, Collectors.counting()));
        model.addAttribute("workHours", workHourRepository.findAll());
        model.addAttribute("shiftStats", shiftStats);
        return "view-schedule";
    }

    @PostMapping("/view-schedule/{id}/delete")
    public String deleteWorkHour(@PathVariable("id") int id) {
        workHourRepository.deleteById(id);
        return "redirect:/view-schedule";
    }

}
