package gui_elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import gui_elements.controlpanel1.*;
import gui_elements.controlpanel2.*;
import gui_elements.previewpanel.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CardDesignerGUI {
    private JFrame frame;
    //private JLabel frameLabel, backgroundLabel, textBoxLabel;
    private BufferedImage cardFrame, cardBackground, cardTextBox, cardTitleImage,cardItemImage, cardWeight, cardType, cardRune, cardEnchant, cardRange, cardTargets, cardDamageType;
    private PreviewPanel previewPanel;
    ControlPanel1 controlPanel;
    ControlPanel2 controlPanel2;

    public void loadImagePreviewPanel(String i){
        previewPanel.loadImage(i);
    }
    
    public JPanel getPreviewPanel() {
        return previewPanel.panel;
    }

    public BufferedImage getCardType(){
        return cardType;
    }

    public BufferedImage getTitleImage(){
        return cardTitleImage;
    }

    public JFrame getFrame(){
        return frame;
    }

    public BufferedImage getCardBackground(){
        return cardBackground;
    }
    public BufferedImage getCardTextbox(){
        return cardTextBox;
    }
    public BufferedImage getCardFrame(){
        return cardFrame;
    }
    public BufferedImage getCardItemImage(){
        return cardItemImage;
    }

    public void setCardFrame(BufferedImage i){
        cardFrame = i;
        previewPanel.repaint();
    }
    public void setCardTextbox(BufferedImage i){
        cardTextBox = i;
        previewPanel.repaint();
    }
    public void setCardBackground(BufferedImage i){
        cardBackground = i;
        previewPanel.repaint();
    }
    public void setCardType(BufferedImage i){
        cardType = i;
        previewPanel.repaint();
    }

    public void setCardTitleImage(BufferedImage i){
        cardTitleImage = i;
        previewPanel.repaint();
    }
    public void setCardItemImage(BufferedImage i){
        cardItemImage = i;
        previewPanel.repaint();
    }

    public void onButtonWeapon(){
        try{
            setCardType(ImageIO.read(new File("resources/weapon.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Weapon; Icon File not found");
        }
        controlPanel.setItemArtType(0);
    }

    public void onButtonArmor(){
        try{
            setCardType(ImageIO.read(new File("resources/armor.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Armor; Icon File not found");
        }
        if(controlPanel.getItemArtType()!=1)
            controlPanel.setItemArtType(1);
    }

    public void onButtonClothing(){
        try{
            setCardType(ImageIO.read(new File("resources/clothing.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Clothing; Icon File not found");
        }
    }

    public void onButtonAccessoire(){
        try{
            setCardType(ImageIO.read(new File("resources/accessoire.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Accessoire; Icon File not found");
        }
        controlPanel.setItemArtType(3);
    }

    public void onButtonConsumable(){
        try{
            setCardType(ImageIO.read(new File("resources/consumable.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Consumable; Icon File not found");
        }
        controlPanel.setItemArtType(4);
    }






    public CardDesignerGUI() {

        frame = new JFrame("Card Designer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(null);
        
        // Preview Panel on the left
        previewPanel = new PreviewPanel(this);
        previewPanel.loadDefault();

        // Control Panel on the right
        controlPanel = new ControlPanel1();
        controlPanel.init(this);

        controlPanel2 = new ControlPanel2();
        controlPanel2.init(this);
        
        frame.add(previewPanel.panel);
        frame.add(controlPanel);
        frame.add(controlPanel2, BorderLayout.SOUTH);
        frame.setVisible(true);

        frame.setLayout(null);

        SwingUtilities.invokeLater(() -> {
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    rescaleComponents(); // This should be called after all components are loaded
                }
            });
            frame.addWindowStateListener(new WindowStateListener() {
                @Override
                public void windowStateChanged(WindowEvent e) {
                    if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
                        rescaleComponents();
                    } else {
                        rescaleComponents();
                    }
                }
            });
        });
    }

    public double getFrameScale() {
        double scaleX = (double) frame.getWidth() / 1920.0;
        double scaleY = (double) frame.getHeight() / 1080.0;
        //System.out.println(Math.min(scaleX, scaleY));
        return Math.min(scaleX, scaleY);
    }

    private void rescaleComponents(){
        double scale = getFrameScale();

        previewPanel.rescale(scale);
        controlPanel.rescale(scale);
        controlPanel2.rescale(scale);
    }

    public void exportImage() {
        int targetWidth = 750;
        int targetHeight = 1050;

        double scale = Math.min((750.0/555.0),(1050.0/735.0));

        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cardBackground != null) {
            g2d.drawImage(cardBackground, 0, 0, 750, 1050, null);
        }

        if (cardFrame != null) {
            g2d.drawImage(cardFrame, 0, 0, 750, 1050, null);
        }

        if (cardItemImage != null) {
            g2d.drawImage(cardItemImage, (int)(93*scale), (int)(80*scale),  (int)(400*scale),  (int)(400*scale), null);
        }

        if (cardTextBox != null) {
            g2d.drawImage(cardTextBox, 0, 0, 750, 1050, null);
        }

        if (cardType != null) {
            g2d.drawImage(cardType, (int)(410*scale), (int)(372*scale),  (int)(128*scale),  (int)(128*scale), null);
        }

        if (cardTitleImage != null) {
            g2d.drawImage(cardTitleImage, 0, 0, 750, 1050, null);
        }
        

        try {
            File outputfile = new File("export//"+generateDateTimeString()+".png");
            ImageIO.write(finalImage, "PNG", outputfile);
            JOptionPane.showMessageDialog(frame, "Image exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        g2d.dispose();
        
    }

    public static String generateDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Date now = new Date();
        return sdf.format(now);
    }



    public static void main(String[] args) {
        try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
        // handle exception
        }
        catch (ClassNotFoundException e) {
        // handle exception
        }
        catch (InstantiationException e) {
        // handle exception
        }
        catch (IllegalAccessException e) {
        // handle exception
        }
        SwingUtilities.invokeLater(CardDesignerGUI::new);
    }

    
}
