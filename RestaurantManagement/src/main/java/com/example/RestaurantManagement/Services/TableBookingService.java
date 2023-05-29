package com.example.RestaurantManagement.Services;

import com.example.RestaurantManagement.Models.TableBooking;
import com.example.RestaurantManagement.Models.Tables;
import com.example.RestaurantManagement.Repositories.TableBookingRepository;
import com.example.RestaurantManagement.Repositories.TablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class TableBookingService {

    private final TableBookingRepository tableBookingRepository;
    private final TablesRepository tablesRepository;

    @Autowired
    public TableBookingService(TableBookingRepository tableBookingRepository,
                               TablesRepository tablesRepository) {
        this.tableBookingRepository = tableBookingRepository;
        this.tablesRepository = tablesRepository;
    }

    public List<Tables> getAllTables() {
        return tablesRepository.findAll();
    }

    public List<TableBooking> getAllBookings() {
        return tableBookingRepository.findAll();
    }

    public String bookTable(int tableId, Date date, Time time, String info) {
        Tables table = tablesRepository.findById(tableId).orElse(null);
        if (table == null) {
            return "Столик не найден";
        }

        TableBooking booking = new TableBooking();
        booking.setTable(table);
        booking.setDate(date);
        booking.setTime(time);
        booking.setInfo(info);
        tableBookingRepository.save(booking);

        return "Столик успешно забронирован";
    }

    public boolean isTableBooked(int tableId, Date date, Time time) {
        List<TableBooking> bookings = this.getAllBookings();
        for (TableBooking booking : bookings) {
            if (booking.getTable().getId() == tableId && booking.getDate().equals(date) && booking.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }

    public void deleteBooking(int id) {
        tableBookingRepository.deleteById(id);
    }

}
