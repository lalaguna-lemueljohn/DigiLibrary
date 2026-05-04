package com.bookstore.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private LocalDateTime createdAt;
    private int customerId;
    private List<OrderDetail> details = new ArrayList<>();
    private double subtotal;
    private double discountAmount;
    private double total;

    public Order(long id, LocalDateTime createdAt, int customerId) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerId = customerId;
    }

    public long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getCustomerId() { return customerId; }
    public List<OrderDetail> getDetails() { return details; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTotal() { return total; }

    public void addDetail(OrderDetail detail) {
        details.add(detail);
    }

    public void finalizeTotals(double discountRate) {
        subtotal = details.stream().mapToDouble(OrderDetail::getLineTotal).sum();
        discountAmount = subtotal * discountRate;
        total = subtotal - discountAmount;
    }
}
