package com.bookstore.model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookId;
    private String title;
    private int quantity;
    private double unitPrice;

    public OrderDetail(int bookId, String title, int quantity, double unitPrice) {
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getLineTotal() { return quantity * unitPrice; }
}
