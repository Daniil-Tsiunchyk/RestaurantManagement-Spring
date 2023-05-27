package com.example.RestaurantManagement.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderRequest {

    private String information;
    private double totalCost;
    private Tables table;
    @JsonProperty("dish_ids")

    private List<Integer> dish_ids;

    public List<Integer> getDish_ids() {
        return dish_ids;
    }

    public void setDish_ids(List<Integer> dish_ids) {
        this.dish_ids = dish_ids;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Tables getTable() {
        return table;
    }

    public void setTable(Tables table) {
        this.table = table;
    }

}