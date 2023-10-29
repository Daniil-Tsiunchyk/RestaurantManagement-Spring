package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "dishes")
public class Dish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "cost")
  private double cost;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private DishType type;

  @Column(name = "description", nullable = true)
  private String description;

  @Column(name = "recipe", nullable = true)
  private String recipe;

  @Lob
  @Column(name = "image", nullable = true)
  private byte[] image;
}
