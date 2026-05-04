package com.bookstore.main;

import com.bookstore.ui.LoginFrame;
import com.bookstore.util.DataStore;
import com.bookstore.util.ThemeManager;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DataStore.initializeData();
        ThemeManager.getMode();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
