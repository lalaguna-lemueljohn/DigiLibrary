package com.bookstore.ui;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.User;
import com.bookstore.util.DataStore;
import com.bookstore.util.ThemeManager;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainPOSFrame extends JFrame {
    private final Map<Integer, OrderDetail> cart = new LinkedHashMap<>();
    private final DefaultTableModel cartModel = new DefaultTableModel(new String[]{"Title", "Qty", "Price", "Line Total"}, 0);
    private final JLabel totalLabel = new JLabel("Total: $0.00");
    private final JPanel catalogPanel = new CatalogPanel();

    public MainPOSFrame(User user) {
        setTitle("Bookstore POS - " + user.getUsername());
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextField search = new JTextField(22);
        JComboBox<Customer> customerCombo = new JComboBox<>(DataStore.getCustomers().toArray(new Customer[0]));
        JButton toggleTheme = new JButton("Toggle Theme");

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(new JLabel("Search:"));north.add(search);
        north.add(new JLabel("Customer:"));north.add(customerCombo);
        north.add(toggleTheme);
        add(north, BorderLayout.NORTH);

        catalogPanel.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT, 10, 10));
        add(new JScrollPane(catalogPanel), BorderLayout.CENTER);

        JPanel east = new JPanel(new BorderLayout());
        JTable cartTable = new JTable(cartModel);
        JButton checkout = new JButton("Checkout");
        JPanel bottom = new JPanel(new GridLayout(2,1));
        bottom.add(totalLabel); bottom.add(checkout);
        east.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        east.add(bottom, BorderLayout.SOUTH);
        east.setPreferredSize(new java.awt.Dimension(360, 0));
        add(east, BorderLayout.EAST);

        refreshCatalog("");

        search.getDocument().addDocumentListener((SimpleDocumentListener) e -> refreshCatalog(search.getText().trim().toLowerCase()));
        toggleTheme.addActionListener(e -> {
            ThemeManager.toggleMode();
            catalogPanel.repaint();
            refreshCatalog(search.getText().trim().toLowerCase());
        });

        checkout.addActionListener(e -> checkout((Customer) customerCombo.getSelectedItem()));
    }

    private void refreshCatalog(String query) {
        catalogPanel.removeAll();
        DataStore.getBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(query) || b.getCategory().toLowerCase().contains(query))
                .forEach(book -> catalogPanel.add(new BookCard(book, this::addToCart)));
        catalogPanel.revalidate();
        catalogPanel.repaint();
    }

    private void addToCart(Book book) {
        if (book.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Out of stock", "Stock Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        OrderDetail existing = cart.get(book.getId());
        int currentQty = existing == null ? 0 : existing.getQuantity();
        if (currentQty + 1 > book.getStock()) {
            JOptionPane.showMessageDialog(this, "Not enough stock for more of this item.", "Stock Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cart.put(book.getId(), new OrderDetail(book.getId(), book.getTitle(), currentQty + 1, book.getPrice()));
        refreshCartTable();
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        double total = 0;
        for (OrderDetail d : cart.values()) {
            double lineTotal = d.getLineTotal();
            total += lineTotal;
            cartModel.addRow(new Object[]{d.getTitle(), d.getQuantity(), String.format("$%.2f", d.getUnitPrice()), String.format("$%.2f", lineTotal)});
        }
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void checkout(Customer customer) {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        long orderId = System.currentTimeMillis();
        Order order = new Order(orderId, LocalDateTime.now(), customer.getId());
        cart.values().forEach(order::addDetail);
        order.finalizeTotals(customer.getDiscountRate());

        for (OrderDetail detail : cart.values()) {
            Book book = DataStore.getBooks().stream().filter(b -> b.getId() == detail.getBookId()).findFirst().orElse(null);
            if (book != null) {
                book.setStock(book.getStock() - detail.getQuantity());
            }
        }

        DataStore.getOrders().add(order);
        DataStore.saveData();
        writeReceipt(order, customer);

        cart.clear();
        refreshCartTable();
        refreshCatalog("");
        JOptionPane.showMessageDialog(this, "Checkout complete!");
    }

    private void writeReceipt(Order order, Customer customer) {
        String filename = "receipt_" + System.currentTimeMillis() + ".txt";
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("=== DigiLibrary Receipt ===\n");
            fw.write("Order ID: " + order.getId() + "\n");
            fw.write("Customer: " + customer.getName() + "\n\n");
            for (OrderDetail d : order.getDetails()) {
                fw.write(String.format("%s x%d @ $%.2f = $%.2f%n", d.getTitle(), d.getQuantity(), d.getUnitPrice(), d.getLineTotal()));
            }
            fw.write("\nSubtotal: $" + String.format("%.2f", order.getSubtotal()) + "\n");
            fw.write("Discount: -$" + String.format("%.2f", order.getDiscountAmount()) + "\n");
            fw.write("Total: $" + String.format("%.2f", order.getTotal()) + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to write receipt: " + ex.getMessage());
        }
    }

    private static class CatalogPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (ThemeManager.getMode() == ThemeManager.Mode.DARK) {
                try {
                    BufferedImage bg = ImageIO.read(new File("src/assets/themes/starry_night.jpg"));
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                } catch (Exception e) {
                    g.setColor(new Color(15, 18, 46));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            } else {
                g.setColor(ThemeManager.background());
                g.fillRect(0,0,getWidth(),getHeight());
            }
        }
    }

    @FunctionalInterface
    private interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update(javax.swing.event.DocumentEvent e);
        default void insertUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        default void removeUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        default void changedUpdate(javax.swing.event.DocumentEvent e) { update(e); }
    }
}
