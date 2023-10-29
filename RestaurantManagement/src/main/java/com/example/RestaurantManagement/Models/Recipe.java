package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "recipes")
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int idrecipes;

  @Column(name = "information")
  private String information;

  @Column(name = "instruction")
  private String instruction;

  @Column(name = "photo")
  private String photo;
}
