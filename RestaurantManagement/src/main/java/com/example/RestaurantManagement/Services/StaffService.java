package com.example.RestaurantManagement.Services;

import com.example.RestaurantManagement.Models.Staff;
import com.example.RestaurantManagement.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public void saveStaff(Staff staff) {
        staffRepository.save(staff);
    }

    public void updateStaff(Staff staff) {
        staffRepository.save(staff);
    }

    public void deleteStaff(Staff staff) {
        staffRepository.delete(staff);
    }

    public Staff getStaffById(int id) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            return staff.get();
        } else {
            throw new RuntimeException("Staff not found for id: " + id);
        }
    }
}
