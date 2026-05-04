package com.bookstore.ui;

import com.bookstore.model.Book;
import com.bookstore.util.ThemeManager;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class BookCard extends JPanel {
    public interface AddToCartListener { void onAdd(Book book); }

    public BookCard(Book book, AddToCartListener listener) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(ThemeManager.primary(), 1));
        setPreferredSize(new Dimension(150, 280));
        setBackground(ThemeManager.background());

        add(createImagePanel(book), BorderLayout.CENTER);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel title = new JLabel("<html><div style='text-align:center;'>" + book.getTitle() + "</div></html>");
        title.setForeground(ThemeManager.text());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 12f));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel price = new JLabel(String.format("$%.2f", book.getPrice()));
        price.setForeground(ThemeManager.text());
        price.setAlignmentX(CENTER_ALIGNMENT);

        JButton add = new JButton("Add to Cart");
        add.addActionListener(e -> listener.onAdd(book));

        info.add(title);
        info.add(price);
        info.add(add);

        add(info, BorderLayout.SOUTH);
    }

    private JPanel createImagePanel(Book book) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 180));
        panel.setOpaque(true);

        try {
            BufferedImage image = ImageIO.read(new File(book.getImagePath()));
            if (image == null) throw new IllegalStateException("Image not found");
            Image scaled = image.getScaledInstance(120, 180, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel();
            imageLabel.setIcon(new javax.swing.ImageIcon(scaled));
            panel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            panel.setBackground(Color.DARK_GRAY);
            JLabel fallbackTitle = new JLabel("<html><div style=\"text-align:center;color:white;\">" + book.getTitle() + "</div></html>", JLabel.CENTER);
            panel.add(fallbackTitle, BorderLayout.CENTER);
        }
        return panel;
    }
}
