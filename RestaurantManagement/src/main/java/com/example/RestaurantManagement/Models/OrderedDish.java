package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ordered_dish")
public class OrderedDish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "dish_id")
  private Dish dish;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "status")
  private String status;
}
