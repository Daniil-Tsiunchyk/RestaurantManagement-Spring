package com.example.RestaurantManagement.Repositories;

import com.example.RestaurantManagement.Models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findStaffByLogin(String login);

    Staff findByLogin(String login);
}
