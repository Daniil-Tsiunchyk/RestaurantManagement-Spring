package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Time;
import lombok.Data;

@Entity
@Data
@Table(name = "tables_booking")
public class TableBooking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "table_id")
  private Tables table;

  @Column(name = "date")
  private Date date;

  @Column(name = "time")
  private Time time;

  @Column(name = "info")
  private String info;
}
