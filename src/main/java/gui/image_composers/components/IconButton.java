package gui.image_composers.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import events.EventBus;

import java.awt.Image;
import java.awt.event.ActionListener;

public class IconButton extends JButton{
    private String id;

    public IconButton(String path, Dimension dim,String id, ActionListener e){
        this.id = id;
        this.setIcon(loadIcon(path, dim));
        this.setBackground(Color.GRAY);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addActionListener(e);
    }

    private ImageIcon loadIcon(String path, Dimension dim) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image scaledImage = originalIcon.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
