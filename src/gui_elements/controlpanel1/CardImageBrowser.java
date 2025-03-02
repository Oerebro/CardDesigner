package gui_elements.controlpanel1;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import gui_elements.CardDesignerGUI;
import abstractclasses.*;

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
                case 'f': parent.setCardFrame(ImageIO.read(file));break;
                case 'b': parent.setCardBackground(ImageIO.read(file));break;
                case 't': parent.setCardTextbox(ImageIO.read(file));break;
                case 'h': parent.setCardTitleImage(ImageIO.read(file));break;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading new image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
