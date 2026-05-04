package com.bookstore.model;

import java.io.Serializable;

public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String phone;

    public Supplier(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return name;
    }
}
