package gui.controlpanel1;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import gui.*;
import abstractclasses.*;

import java.awt.Color;


public class CardImageBrowser extends ImageBrowser{
    private final CardDesignerGUI parent;
    private char type;

    public CardImageBrowser(CardDesignerGUI parent, String path, int width, int height,int x, int y, int iconSize,char type) {
        this.parent = parent;
        this.path = path;
        this.width = width;
        this.height = height;
        this.iconSize = iconSize;
        this.type = type;

        init();
        rescale(1.0);
    }


    protected void updateImage(File file) {
        try {
            switch(type){
                case 'f': parent.setImageComposer("cardFrame",ImageIO.read(file));break;
                case 'b': parent.setImageComposer("cardBackground",ImageIO.read(file));break;
                case 't': parent.setImageComposer("cardTextbox",ImageIO.read(file));break;
                case 'h': parent.setImageComposer("cardTitle",ImageIO.read(file));break;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading new image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected IconLabel addMouseListener(IconLabel label, File file) {
        label.setOpaque(true);  // Important: This allows the background color to show.
        label.setBackground(Color.LIGHT_GRAY);  // Set light grey background.

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                updateImage(file);
            }
        });

        return label;
    }
}
