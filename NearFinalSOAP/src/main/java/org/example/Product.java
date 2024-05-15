package org.example;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private int quantity;
    private double price;

    public Product(){
    }

    public Product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setName(String name){this.name = name;}
    public void setPrice(double price){this.price = price;}
}
