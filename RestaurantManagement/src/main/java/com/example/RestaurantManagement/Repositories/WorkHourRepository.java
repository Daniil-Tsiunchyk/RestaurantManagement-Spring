package com.example.RestaurantManagement.Repositories;

import com.example.RestaurantManagement.Models.WorkHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkHourRepository extends JpaRepository<WorkHour, Integer> {}
