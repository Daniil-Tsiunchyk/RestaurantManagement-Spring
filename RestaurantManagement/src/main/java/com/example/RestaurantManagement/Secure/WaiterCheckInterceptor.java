package com.example.RestaurantManagement.Secure;

import com.example.RestaurantManagement.Models.Staff;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class WaiterCheckInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler
  ) throws Exception {
    HttpSession session = request.getSession();
    Staff user = (Staff) session.getAttribute("staff");
    if (user == null || !user.getRole().equals("ОФИЦИАНТ")) {
      response.sendRedirect("/login");
      return false;
    }
    return true;
  }
}
