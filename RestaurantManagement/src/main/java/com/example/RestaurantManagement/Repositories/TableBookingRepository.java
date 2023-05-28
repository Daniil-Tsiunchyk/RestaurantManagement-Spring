package com.example.RestaurantManagement.Repositories;

import com.example.RestaurantManagement.Models.TableBooking;
import com.example.RestaurantManagement.Models.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public interface TableBookingRepository extends JpaRepository<TableBooking, Integer> {

}
