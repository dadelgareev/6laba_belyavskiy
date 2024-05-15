package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private String customerName;
    private String address;
    private String status;
    private List<OrderItem> orderItems;

    public Order() {
        this.orderItems = new ArrayList<>();
    }

    public Order(int id, String customerName, String address) {
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.status = "Оформлен"; // Устанавливаем статус заказа по умолчанию
        this.orderItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getProduct().getPrice() * orderItem.getQuantity();
        }
        return totalPrice;
    }

    // Другие методы, если необходимо
}
