package gui_elements.previewpanel;
import javax.swing.*;

import gui_elements.CardDesignerGUI;
import gui_elements.controlpanel1.FontLoader;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PreviewPanel {
    public JPanel panel;
    private JPanel object;
    private CardDesignerGUI parent;
    private JLabel titleTextDisplay;

    public PreviewPanel(CardDesignerGUI parent){
        this.parent = parent;
        init();
    }

    public JLabel getTitleTextDisplay() {
        JLabel newLabel = new JLabel(titleTextDisplay.getText());
        newLabel.setFont(titleTextDisplay.getFont());
        newLabel.setForeground(titleTextDisplay.getForeground());
        newLabel.setHorizontalAlignment(titleTextDisplay.getHorizontalAlignment());
        newLabel.setVerticalAlignment(titleTextDisplay.getVerticalAlignment());
        newLabel.setOpaque(titleTextDisplay.isOpaque());

        return newLabel;
    }
    
    private void init() {
        int scaledWidth = (int) (parent.getFrameScale() * (750*0.7));
        int scaledHeight = (int) (parent.getFrameScale() * (1050*0.7));

        object = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                double scale = parent.getFrameScale();

                

                System.out.println(scaledHeight + " "+ scaledWidth);

                BufferedImage bg = parent.getCardBackground();
                BufferedImage tb = parent.getCardTextbox();
                BufferedImage ct = parent.getCardType();
                BufferedImage cf = parent.getCardFrame();
                BufferedImage ii = parent.getCardItemImage();
                BufferedImage ti = parent.getTitleImage();
                BufferedImage hi = parent.getCardHandedImage();

                if (bg != null) {
                    g.drawImage(bg, 0, 0, scaledWidth, scaledHeight, this);
                }
    
                if (cf != null) {
                    g.drawImage(cf, 0, 0, scaledWidth, scaledHeight, this);
                }

                try{
                    g.drawImage(ImageIO.read(new File("resources/misc/frontborder.png")), 0, 0, scaledWidth, scaledHeight, this);
                }catch(IOException e){};

                if (ii != null) {
                    g.drawImage(ii, (int)(97*scale), (int)(80*scale), (int)(400*scale), (int)(400*scale), this);
                }
    
                if (tb != null) {
                    g.drawImage(tb, 0, 0, scaledWidth, scaledHeight, this);
                }
    
                if (ct != null) {
                    g.drawImage(ct, (int) (410*scale), (int)(372*scale), (int) (128*scale), (int) (128*scale), this);
                }

                if (ti != null) {
                    g.drawImage(ti, 0, (int) (10*scale), scaledWidth, scaledHeight, this);
                } 
                
                if (hi != null) {
                    g.drawImage(hi, 498, 650, (int)(60*scale), (int)(60*scale), this);
                } 
            }
        };
    
        object.setLayout(null);
        object.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

        titleTextDisplay = new JLabel("", SwingConstants.CENTER);
        //titleTextDisplay.setBounds(70, 10, 405, 50); // Position at (0,0) with size 200x300
        titleTextDisplay.setOpaque(false);

        //temporary measure to make title text white
        titleTextDisplay.setForeground(Color.WHITE);

        object.add(titleTextDisplay);

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 1100));
        panel.add(object, BorderLayout.LINE_START);
        

        

        rescale(1.0);
    
        
    }

    public void updateTitleTextDisplay(String str, Font font) {
        //System.out.println("Test preview updateTitleTextDisplay: "+str+" Font: "+font.getName());
        font = FontLoader.loadFont(font.getName(), 72f);
        Font scaledFont = getScaledFont(str, font, titleTextDisplay.getWidth(), titleTextDisplay.getHeight());
        titleTextDisplay.setText("<html><div style='text-align:center;'>" + str + "</div></html>");
        titleTextDisplay.setFont(scaledFont);
        titleTextDisplay.repaint();
    }

    private Font getScaledFont(String text, Font baseFont, int maxWidth, int maxHeight) {
        int fontSize = baseFont.getSize();
        FontMetrics metrics;
        do {
            fontSize--;
            Font tempFont = baseFont.deriveFont((float) fontSize);
            metrics = titleTextDisplay.getFontMetrics(tempFont);
        } while (metrics.stringWidth(text) > maxWidth || metrics.getHeight() > maxHeight);

        return baseFont.deriveFont((float) fontSize);
    }

    public void loadDefault() {
        try{
            parent.setCardFrame(ImageIO.read(new File("resources/frame/default.png")));
            parent.setCardTitleImage(ImageIO.read(new File("resources/title/default.png")));
            parent.setCardTextbox(ImageIO.read(new File("resources/textbox/default.png")));
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
        int scaledWidth = (int) (parent.getFrameScale() * (750*0.7));
        int scaledHeight = (int) (parent.getFrameScale() * (1050*0.7));

        panel.setBounds((int) (10*scale), (int) (10*scale), (int) (scaledWidth*scale), (int) (scaledHeight*scale));
        object.setPreferredSize(new Dimension((int) (scaledWidth*scale), (int) (scaledHeight*scale)));
        panel.repaint();

        titleTextDisplay.setBounds((int) (60*scale), (int) (20*scale), (int) (405*scale), (int) (50*scale));
    }

}


