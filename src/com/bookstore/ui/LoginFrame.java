package com.bookstore.ui;

import com.bookstore.model.User;
import com.bookstore.util.DataStore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Bookstore POS - Login");
        setSize(320, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);

        JButton loginBtn = new JButton("Login");
        panel.add(new JLabel());
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String u = username.getText().trim();
            String p = new String(password.getPassword());
            User user = DataStore.getUsers().stream()
                    .filter(x -> x.getUsername().equals(u) && x.getPassword().equals(p))
                    .findFirst().orElse(null);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
            new MainPOSFrame(user).setVisible(true);
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                new DashboardFrame().setVisible(true);
            }
        });

        setContentPane(panel);
    }
}
