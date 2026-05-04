package com.bookstore.ui;

import com.bookstore.model.Book;
import com.bookstore.model.Supplier;
import com.bookstore.util.DataStore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Admin Dashboard");
        setSize(640, 420);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Low Stock", lowStockPanel());
        tabs.add("Suppliers", supplierPanel());
        setContentPane(tabs);
    }

    private JPanel lowStockPanel() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Title", "Stock"}, 0);
        for (Book b : DataStore.getBooks()) {
            if (b.getStock() < 5) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getStock()});
            }
        }
        return new JPanel(new BorderLayout()) {{ add(new JScrollPane(new JTable(model))); }};
    }

    private JPanel supplierPanel() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Phone"}, 0);
        DataStore.getSuppliers().forEach(s -> model.addRow(new Object[]{s.getId(), s.getName(), s.getPhone()}));
        JTable table = new JTable(model);

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Supplier name");
            String phone = JOptionPane.showInputDialog(this, "Phone");
            if (name == null || phone == null || name.isBlank() || phone.isBlank()) return;
            int id = DataStore.getSuppliers().stream().mapToInt(Supplier::getId).max().orElse(0) + 1;
            Supplier s = new Supplier(id, name, phone);
            DataStore.getSuppliers().add(s);
            model.addRow(new Object[]{id, name, phone});
            DataStore.saveData();
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) return;
            int id = (Integer) model.getValueAt(r, 0);
            Supplier s = DataStore.getSuppliers().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
            if (s == null) return;
            String name = JOptionPane.showInputDialog(this, "Supplier name", s.getName());
            String phone = JOptionPane.showInputDialog(this, "Phone", s.getPhone());
            if (name == null || phone == null || name.isBlank() || phone.isBlank()) return;
            s.setName(name);
            s.setPhone(phone);
            model.setValueAt(name, r, 1);
            model.setValueAt(phone, r, 2);
            DataStore.saveData();
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) return;
            int id = (Integer) model.getValueAt(r, 0);
            DataStore.getSuppliers().removeIf(x -> x.getId() == id);
            model.removeRow(r);
            DataStore.saveData();
        });

        JPanel buttons = new JPanel();
        buttons.add(add);buttons.add(edit);buttons.add(delete);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }
}
