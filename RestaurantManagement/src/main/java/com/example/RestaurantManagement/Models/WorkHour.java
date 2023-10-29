package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import java.sql.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "work_hours")
public class WorkHour {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "staff_id")
  private Staff staff;

  @Column(name = "date")
  private Date date;
}
