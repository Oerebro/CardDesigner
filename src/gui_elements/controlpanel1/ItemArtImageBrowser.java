package gui_elements.controlpanel1;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import gui_elements.CardDesignerGUI;
import abstractclasses.*;

public class ItemArtImageBrowser extends ImageBrowser{
    private final CardDesignerGUI parent;

    public ItemArtImageBrowser(CardDesignerGUI parent, String path, int width, int height, int x, int y, int iconSize) {
        //super(parent, path, width, height, 'I');
        this.parent = parent;
        this.path = path;
        this.width = width;
        this.height = height;
        this.iconSize = iconSize;

        init();
        rescale(1.0);
    }

    protected void updateImage(File file) {
        try {
            //System.out.println("updateImage");
            parent.setCardItemImage(ImageIO.read(file));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading new image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
