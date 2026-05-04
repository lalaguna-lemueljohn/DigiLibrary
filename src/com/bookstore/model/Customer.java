package com.bookstore.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private double discountRate;

    public Customer(int id, String name, String email, double discountRate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.discountRate = discountRate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getDiscountRate() { return discountRate; }

    @Override
    public String toString() {
        return name + " (" + (int) (discountRate * 100) + "% off)";
    }
}
