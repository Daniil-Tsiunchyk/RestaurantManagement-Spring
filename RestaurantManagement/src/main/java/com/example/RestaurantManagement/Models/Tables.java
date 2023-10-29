package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tables")
public class Tables {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "seats")
  private int seats;
}
