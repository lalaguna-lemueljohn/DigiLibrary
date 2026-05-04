package com.bookstore.model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String author;
    private String category;
    private double price;
    private int stock;
    private String imagePath;
    private int supplierId;

    public Book(int id, String title, String author, String category, double price, int stock, String imagePath, int supplierId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
        this.supplierId = supplierId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImagePath() { return imagePath; }
    public int getSupplierId() { return supplierId; }

    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return title + " - $" + String.format("%.2f", price);
    }
}
