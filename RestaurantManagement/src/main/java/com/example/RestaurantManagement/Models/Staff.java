package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import java.sql.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "staff")
public class Staff {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "login")
  private String login;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  private String role;

  @Column(name = "apparatus_employed")
  private Date apparatusEmployed;

  @Column(name = "dismissal_from_work")
  private Date dismissalFromWork;
}
