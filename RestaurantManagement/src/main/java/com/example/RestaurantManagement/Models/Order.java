package com.example.RestaurantManagement.Models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "information")
  private String information;

  @Column(name = "total_cost")
  private double totalCost;

  @ManyToOne
  @JoinColumn(name = "table_id")
  private Tables table;

  @Column(name = "start_time")
  private Timestamp startTime;

  @Column(name = "end_time")
  private Timestamp endTime;

  @Column(name = "status")
  private String status;

  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
  private List<OrderedDish> orderedDishes;

  public List<OrderedDish> getOrderedDishes() {
    return orderedDishes;
  }
}
