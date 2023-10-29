package com.example.RestaurantManagement.Controllers;

import com.example.RestaurantManagement.Models.Staff;
import com.example.RestaurantManagement.Services.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StaffController {

  @Autowired
  private HttpSession session;

  private final StaffService staffService;

  @Autowired
  public StaffController(StaffService staffService) {
    this.staffService = staffService;
  }

  @GetMapping("/staff")
  public String getStaff(Model model) {
    model.addAttribute("staff", staffService.getAllStaff());
    model.addAttribute("newStaff", new Staff());
    model.addAttribute("currentUser", getCurrentUser());
    return "staff";
  }

  @PostMapping("/staff/add")
  public String addStaff(@ModelAttribute Staff staff, Model model) {
    if (staffService.loginExists(staff.getLogin())) {
      model.addAttribute("error", "Логин уже существует!");
      model.addAttribute("staff", staffService.getAllStaff());
      model.addAttribute("newStaff", staff);
      return "redirect:/staff";
    }

    java.util.Date currentDate = new java.util.Date();
    staff.setApparatusEmployed(new java.sql.Date(currentDate.getTime()));
    staffService.saveStaff(staff);
    return "redirect:/staff";
  }

  @PostMapping("/staff/update")
  public String updateStaff(
    @RequestParam("id") int id,
    @RequestParam("role") String role
  ) {
    Staff staff = staffService.getStaffById(id);
    staff.setRole(role);
    staffService.updateStaff(staff);
    return "redirect:/staff";
  }

  @PostMapping("/staff/delete")
  public String deleteStaff(@RequestParam("id") int id) {
    Staff staff = staffService.getStaffById(id);
    staffService.deleteStaff(staff);
    return "redirect:/staff";
  }

  public Staff getCurrentUser() {
    return (Staff) session.getAttribute("staff");
  }
}
