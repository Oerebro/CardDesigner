package gui_elements.previewpanel;
import javax.swing.*;

import gui_elements.CardDesignerGUI;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PreviewPanel {
    public JPanel panel;
    private JPanel object;
    private CardDesignerGUI parent;

    public PreviewPanel(CardDesignerGUI parent){
        this.parent = parent;
        init();
    }

    private void init() {
        object = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                double scale = parent.getFrameScale();

                int scaledWidth = (int) (parent.getFrameScale() * 555);
                int scaledHeight = (int) (parent.getFrameScale() * 735);

                BufferedImage bg = parent.getCardBackground();
                BufferedImage tb = parent.getCardTextbox();
                BufferedImage ct = parent.getCardType();
                BufferedImage cf = parent.getCardFrame();
                BufferedImage ii = parent.getCardItemImage();
                BufferedImage ti = parent.getTitleImage();

                if (bg != null) {
                    g.drawImage(bg, 0, 0, scaledWidth, scaledHeight, this);
                }
    
                if (cf != null) {
                    g.drawImage(cf, 0, 0, scaledWidth, scaledHeight, this);
                }

                if (ii != null) {
                    g.drawImage(ii, (int)(93*scale), (int)(80*scale), (int)(400*scale), (int)(400*scale), this);
                }
    
                if (tb != null) {
                    g.drawImage(tb, 0, 0, scaledWidth, scaledHeight, this);
                }
    
                if (ct != null) {
                    g.drawImage(ct, (int) (410*scale), (int)(372*scale), (int) (128*scale), (int) (128*scale), this);
                }

                if (ti != null) {
                    g.drawImage(ti, 0, 0, scaledWidth, scaledHeight, this);
                }               
            }
        };
    
        object.setPreferredSize(new Dimension(555, 735));
        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 1100));
        panel.add(object, BorderLayout.LINE_START);
        rescale(1.0);
    
        
    }

    public void loadDefault() {
        try{
            parent.setCardFrame(ImageIO.read(new File("resources/frame/default.png")));
            //parent.setCardTextbox(ImageIO.read(new File("resources/textbox/default.png")));
            parent.setCardBackground(ImageIO.read(new File("resources/background/default.png")));
        }catch(IOException e){
            JOptionPane.showMessageDialog(parent.getFrame(), "Error loading default images.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadImage(String type) {

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(parent.getFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedImage image = ImageIO.read(file);
                    switch (type) {
                        case "frame":
                            break;
                        case "background":
                            parent.setCardBackground(image);

                            break;
                        case "textbox":
                            parent.setCardTextbox(image);
                            break;
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(parent.getFrame(), "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
    }
    
    public int[] getScaledDimensions(int imageWidth, int imageHeight, int maxWidth, int maxHeight) {
        // Compute scale factors for width and height
        double scaleX = (double) maxWidth / imageWidth;
        double scaleY = (double) maxHeight / imageHeight;
    
        // Choose the smaller scale factor to maintain aspect ratio
        double scaleFactor = Math.min(scaleX, scaleY);
    
        // Apply scaling, making sure neither dimension exceeds maxWidth or maxHeight
        int newWidth = (int) (imageWidth * scaleFactor*parent.getFrameScale());
        int newHeight = (int) (imageHeight * scaleFactor*parent.getFrameScale());
    
        // Return the scaled dimensions
        return new int[]{newWidth, newHeight};
    }

    public void repaint(){
        panel.repaint();
    }

    public void rescale(double scale){
        panel.setBounds(10, 10, (int) (556*scale), (int) (735*scale));
        object.setPreferredSize(new Dimension((int) (555*scale), (int) (735*scale)));
        panel.repaint();
    }

}


