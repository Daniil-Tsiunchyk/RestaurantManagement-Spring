package com.example.RestaurantManagement.Secure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminCheckInterceptor()).addPathPatterns("/menu", "/orders", "/staff");
        registry.addInterceptor(new ManagerCheckInterceptor()).addPathPatterns("/assign-shifts", "/book-table", "/manage-orders", "/view-schedule");
        registry.addInterceptor(new KitchenCheckInterceptor()).addPathPatterns("/kitchen", "/recipe");
        registry.addInterceptor(new WaiterCheckInterceptor()).addPathPatterns("/create-order", "/view-orders");
    }

}
