package com.bookstore.util;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.Supplier;
import com.bookstore.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class DataStore {
    private static final String FILE_NAME = "pos_data.ser";

    private static List<User> users = new ArrayList<>();
    private static List<Book> books = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static List<Supplier> suppliers = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();

    private DataStore() {}

    public static void initializeData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            seedData();
            saveData();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            DataBundle bundle = (DataBundle) ois.readObject();
            users = bundle.users;
            books = bundle.books;
            customers = bundle.customers;
            suppliers = bundle.suppliers;
            orders = bundle.orders;
        } catch (Exception e) {
            seedData();
            saveData();
        }
    }

    public static void saveData() {
        DataBundle bundle = new DataBundle(users, books, customers, suppliers, orders);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(bundle);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

    public static List<User> getUsers() { return users; }
    public static List<Book> getBooks() { return books; }
    public static List<Customer> getCustomers() { return customers; }
    public static List<Supplier> getSuppliers() { return suppliers; }
    public static List<Order> getOrders() { return orders; }

    private static void seedData() {
        users = new ArrayList<>(List.of(
                new User("admin", "admin123", "ADMIN"),
                new User("cashier", "cash123", "CASHIER")
        ));

        suppliers = new ArrayList<>(List.of(
                new Supplier(1, "PaperTrail Distributors", "555-1101"),
                new Supplier(2, "North Star Books Supply", "555-2202")
        ));

        customers = new ArrayList<>(List.of(
                new Customer(1, "Walk-in Customer", "walkin@local", 0.00),
                new Customer(2, "Library Member", "member@local", 0.10)
        ));

        books = new ArrayList<>(List.of(
                new Book(1, "The Hobbit", "J.R.R. Tolkien", "Fantasy", 14.99, 10, "src/assets/covers/the_hobbit.jpg", 1),
                new Book(2, "Dune", "Frank Herbert", "Fantasy", 16.50, 7, "src/assets/covers/dune.jpg", 2),
                new Book(3, "Mistborn", "Brandon Sanderson", "Fantasy", 15.25, 3, "src/assets/covers/mistborn.jpg", 1),
                new Book(4, "Name of the Wind", "Patrick Rothfuss", "Fantasy", 13.75, 6, "src/assets/covers/name_of_the_wind.jpg", 2),
                new Book(5, "Clean Code", "Robert C. Martin", "Technology", 29.99, 9, "src/assets/covers/clean_code.jpg", 1),
                new Book(6, "Effective Java", "Joshua Bloch", "Technology", 34.99, 4, "src/assets/covers/effective_java.jpg", 1),
                new Book(7, "Design Patterns", "GoF", "Technology", 31.00, 2, "src/assets/covers/design_patterns.jpg", 2),
                new Book(8, "Refactoring", "Martin Fowler", "Technology", 30.00, 8, "src/assets/covers/refactoring.jpg", 2),
                new Book(9, "Sapiens", "Yuval Noah Harari", "History", 19.99, 12, "src/assets/covers/sapiens.jpg", 1),
                new Book(10, "Guns Germs and Steel", "Jared Diamond", "History", 18.75, 4, "src/assets/covers/guns_germs_steel.jpg", 2),
                new Book(11, "SPQR", "Mary Beard", "History", 17.50, 5, "src/assets/covers/spqr.jpg", 1),
                new Book(12, "The Silk Roads", "Peter Frankopan", "History", 20.00, 1, "src/assets/covers/the_silk_roads.jpg", 2),
                new Book(13, "Atomic Habits", "James Clear", "Self-Help", 12.99, 14, "src/assets/covers/atomic_habits.jpg", 1),
                new Book(14, "Deep Work", "Cal Newport", "Self-Help", 13.49, 5, "src/assets/covers/deep_work.jpg", 2),
                new Book(15, "Think Again", "Adam Grant", "Self-Help", 11.99, 0, "src/assets/covers/think_again.jpg", 1),
                new Book(16, "Grit", "Angela Duckworth", "Self-Help", 12.50, 3, "src/assets/covers/grit.jpg", 2)
        ));

        orders = new ArrayList<>();
    }

    private static class DataBundle implements Serializable {
        private static final long serialVersionUID = 1L;
        private final List<User> users;
        private final List<Book> books;
        private final List<Customer> customers;
        private final List<Supplier> suppliers;
        private final List<Order> orders;

        private DataBundle(List<User> users, List<Book> books, List<Customer> customers, List<Supplier> suppliers, List<Order> orders) {
            this.users = users;
            this.books = books;
            this.customers = customers;
            this.suppliers = suppliers;
            this.orders = orders;
        }
    }
}
