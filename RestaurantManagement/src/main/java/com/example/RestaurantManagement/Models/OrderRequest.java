package com.example.RestaurantManagement.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class OrderRequest {

  private String information;
  private double totalCost;
  private Tables table;

  @JsonProperty("dish_ids")
  private List<Integer> dish_ids;

  private Map<Integer, Integer> dish_counts;
  private Integer table_id;
}
