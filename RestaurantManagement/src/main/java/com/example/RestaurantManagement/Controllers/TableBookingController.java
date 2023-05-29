package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.TableBooking;
import com.example.RestaurantManagement.Models.Tables;
import com.example.RestaurantManagement.Services.TableBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TableBookingController {

    private final TableBookingService tableBookingService;

    @Autowired
    public TableBookingController(TableBookingService tableBookingService) {
        this.tableBookingService = tableBookingService;
    }

    @GetMapping("/book-table")
    public String bookingForm(Model model) {
        List<TableBooking> bookings = tableBookingService.getAllBookings();
        model.addAttribute("bookings", bookings);

        List<Tables> allTables = tableBookingService.getAllTables();
        model.addAttribute("tables", allTables);

        Date currentDate = Date.valueOf(LocalDate.now());
        Date maxDate = Date.valueOf(LocalDate.now().plusDays(3));

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("maxDate", maxDate);

        List<LocalTime> times = new ArrayList<>();
        for (LocalTime timeOption = LocalTime.of(12, 0);
             timeOption.isBefore(LocalTime.of(20, 30));
             timeOption = timeOption.plusMinutes(30)) {
            times.add(timeOption);
        }

        model.addAttribute("times", times);

        return "book-table";
    }

    @PostMapping("/book-table")
    public String bookTable(@RequestParam("table_id") int tableId,
                            @RequestParam("date") Date date,
                            @RequestParam("time") String time,
                            @RequestParam("info") String info,
                            Model model) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        Time sqlTime = Time.valueOf(localTime);

        String message = tableBookingService.bookTable(tableId, date, sqlTime, info);
        model.addAttribute("message", message);

        List<Tables> allTables = tableBookingService.getAllTables();
        model.addAttribute("tables", allTables);

        Date currentDate = Date.valueOf(LocalDate.now());
        Date maxDate = Date.valueOf(LocalDate.now().plusDays(3));

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("maxDate", maxDate);

        List<LocalTime> times = new ArrayList<>();
        for (LocalTime timeOption = LocalTime.of(12, 0);
             timeOption.isBefore(LocalTime.of(20, 30));
             timeOption = timeOption.plusMinutes(30)) {
            times.add(timeOption);
        }

        model.addAttribute("times", times);

        List<TableBooking> bookings = tableBookingService.getAllBookings();
        model.addAttribute("bookings", bookings);

        return "book-table";
    }

    @PostMapping("/book-table/{id}/delete")
    public String deleteBooking(@PathVariable("id") int id, Model model) {
        tableBookingService.deleteBooking(id);
        return "redirect:/book-table";
    }

}
