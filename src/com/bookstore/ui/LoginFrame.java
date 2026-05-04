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
        setSize(380, 210);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

        panel.add(loginBtn);
        panel.add(signupBtn);

        panel.add(new JLabel("Tip: Admin role only from existing admin users."));
        panel.add(new JLabel());

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

        signupBtn.addActionListener(e -> {
            JTextField newUser = new JTextField();
            JPasswordField newPass = new JPasswordField();
            Object[] fields = {
                    "New username:", newUser,
                    "New password:", newPass
            };
            int result = JOptionPane.showConfirmDialog(this, fields, "Create User (CASHIER)", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            boolean created = DataStore.addUser(newUser.getText(), new String(newPass.getPassword()), "CASHIER");
            if (created) {
                JOptionPane.showMessageDialog(this, "User created successfully. You can login now.");
            } else {
                JOptionPane.showMessageDialog(this, "Could not create user. Username may already exist or fields are empty.", "Sign Up Failed", JOptionPane.WARNING_MESSAGE);
            }
        });

        setContentPane(panel);
    }
}
