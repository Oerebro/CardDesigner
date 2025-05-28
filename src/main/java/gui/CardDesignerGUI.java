package gui;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.formdev.flatlaf.FlatDarkLaf;

import events.EventBus;
import events.ImageUpdateEvent;
import gui.controlpanel1.*;
import gui.controlpanel2.*;
import gui.previewpanel.*;


public class CardDesignerGUI {
    public JFrame frame;
    //private JLabel frameLabel, backgroundLabel, textBoxLabel;
    private ImageComposer imageComposer;
    private PreviewPanel previewPanel;
    ControlPanel1 controlPanel;
    ControlPanel2 controlPanel2;

    public void setTitleColor(Color color){
        previewPanel.updateTitleColor(color);
    }

    public void setInfoColor(Color color){
        previewPanel.updateInfoColor(color);
    }

    /*public void loadImagePreviewPanel(String i){
        previewPanel.loadImage(i);
    }*/
    
    public JPanel getPreviewPanel() {
        return previewPanel.panel;
    }

    public Frame getFrame(){
        return frame;
    }


    public void setRangeAndACFont(String font){
        previewPanel.setRangeAndACFont(font);
    }

    public void setImageComposer(String field, BufferedImage i){
        //imageComposer.setField(field,i);
        previewPanel.repaint();
    }

    public BufferedImage getComposedCard(double scale){
        return imageComposer.composeCard(getFrameScale());
    }


    public void onButtonWeapon(){
        //clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/dice/d6.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Weapon; Icon File not found");
        }
        //controlPanel.itemArtChangeToType(0);
        
    }

    public void onButtonRune(){
        //clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/glyphs/rune.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Rune; Icon File not found");
        }
        //controlPanel.itemArtChangeToType(10);
    }

    public void onButtonEffect(){
        setImageComposer("cardType",null);
        //controlPanel.itemArtChangeToType(5);
    }

    public void onButtonCharacter(){
        try{
            setImageComposer("character",ImageIO.read(new File("resources/character.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Effects; Icon File not found");
        }
        //controlPanel.itemArtChangeToType(6);
    }

    public void onButtonArmor(){
        EventBus.publish(new ImageUpdateEvent("cardType","resources/armor.png"));

        /*if(controlPanel.getItemArtType()!=1){
            controlPanel.itemArtChangeToType(1);
            setImageComposer("cardItemImage",null);
        }*/
        
    }

    public void onButtonClothing(){
        //clearUnrelatedFields();
        try{
           setImageComposer("cardType",ImageIO.read(new File("resources/clothing.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Clothing; Icon File not found");
        }
        /*if(controlPanel.getItemArtType()!=1){
            controlPanel.itemArtChangeToType(1);
            setImageComposer("cardItemImage",null);
        }*/
    }

    public void onButtonAccessoire(){
        //clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/accessoire.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Accessoire; Icon File not found");
        }
        //controlPanel.itemArtChangeToType(3);
        //updateTier(0);
    }

    public void onButtonConsumable(){
        //clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/consumable.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Consumable; Icon File not found");
        }
        //controlPanel.itemArtChangeToType(4);
    }
        


    public CardDesignerGUI() {
        
        imageComposer = new ImageComposer();
        
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
                    rescaleComponents();
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

        return Math.min(scaleX, scaleY);
    }

    private void rescaleComponents(){
        double scale = getFrameScale();

        previewPanel.rescale(scale);
        controlPanel.rescale(scale);
        controlPanel2.rescale(scale);
    }

    public void exportImage() {
        /*int targetWidth = 750;
        int targetHeight = 1050;

        double previewScaleWidth = 750.0*0.7;
        double previewScaleHeight = 1050.0*0.7;*/

        //if higher resolution card is wanted
        double scale = 1.0;

        BufferedImage finalImage = imageComposer.composeCard(1.0);

        Graphics2D g2d = finalImage.createGraphics();

        g2d.setColor(Color.WHITE);
        //g2d.fillRect(0, 0, targetWidth, targetHeight);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (previewPanel.getTitleTextDisplay() != null) {
            JLabel p = previewPanel.copyTitleTextDisplay(scale);
            
            BufferedImage titleText = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D labelGraphics = titleText.createGraphics();
            p.paint(labelGraphics);
            labelGraphics.dispose();
            if(controlPanel.getTitleStroke()){
                titleText = drawStroke(titleText,3,Color.WHITE);
            }
            g2d.drawImage(titleText, (int) (80*scale), (int) (20*scale),  (int) (590*scale),  (int) (80*scale), null);
        }

        if (previewPanel.getInfoTextDisplay() != null) {
            JTextArea p = previewPanel.copyInfoTextDisplay(scale);

            BufferedImage infoText = new BufferedImage((int)(620*scale),  (int)(340*scale), BufferedImage.TYPE_INT_ARGB);

            Graphics2D labelGraphics = infoText.createGraphics();
            p.paint(labelGraphics);
            labelGraphics.dispose();
            g2d.drawImage(infoText, (int) (65*scale), (int)(655*scale),  (int)(620*scale),  (int)(340*scale), null);
        }

        try {
            File outputfile = new File("export//"+generateDateTimeString()+".png");
            ImageIO.write(finalImage, "PNG", outputfile);
            JOptionPane.showMessageDialog(frame, "Image exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error exporting image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


        rescaleComponents();
        
    }

    private BufferedImage drawStroke(BufferedImage src, int strokeWidth, Color color) {
        int w = src.getWidth();
        int h = src.getHeight();

        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.SrcOver);

        // Draw offset copies to simulate a soft stroke (cheap blur)
        for (int dx = -strokeWidth; dx <= strokeWidth; dx++) {
            for (int dy = -strokeWidth; dy <= strokeWidth; dy++) {
                if (dx * dx + dy * dy <= strokeWidth * strokeWidth) {
                    g.drawImage(tintAlpha(src, color), dx, dy, null);
                }
            }
        }

        // Draw the original image on top
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return result;
    }

    private BufferedImage tintAlpha(BufferedImage src, Color color) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage tinted = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = src.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;
                if (alpha > 0) {
                    tinted.setRGB(x, y, (alpha << 24) | (color.getRGB() & 0x00FFFFFF));
                }
            }
        }

        return tinted;
    }




    public static String generateDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Date now = new Date();
        return sdf.format(now);
    }

    public void updateTitleTextDisplay(String str, Font font) {
        previewPanel.updateTitleTextDisplay(str, font);
    }

    public void updateInfoTextDisplay(String str, Font font) {
        previewPanel.updateInfoTextDisplay(str, font);
    }

    

    public void updateRange(String str, Font font) {
        previewPanel.updateRangeText(str, font);
    }

    

    public void updateRuneSlots(int num){
        switch (num){
            case 0: setImageComposer("runeSlot",null);break;
            case 1: try{
                        setImageComposer("runeSlot",ImageIO.read(new File("resources/glyphs/runecharge_1.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneSlots; Icon File not found");
                    }break;
            case 2: try{
                        setImageComposer("runeSlot",ImageIO.read(new File("resources/glyphs/runecharge_2.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneSlots; Icon File not found");
                    }break;
            case 3: try{
                        setImageComposer("runeSlot",ImageIO.read(new File("resources/glyphs/runecharge_3.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneSlots; Icon File not found");
                    }
        }

    }

        public void updateRuneType(String type){
        switch (type){
            case "melee": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_rune_melee.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneType; Icon File not found");
                    }break;
            case "ranged": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_rune_ranged.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneType; Icon File not found");
                    }break;
            case "mixed": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_rune_mixed.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneType; Icon File not found");
                    }break;
            case "armor": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_rune_clothing.png")));
                    }catch(IOException e){
                        throw new Error("Error on RuneType; Icon File not found");
                    }
        }

        

    }

    public void updateRuneCut(Boolean isCut){
        if(isCut){
            try{
                setImageComposer("runeCutTemplate",ImageIO.read(new File("resources/misc/rune_cut.png")));
            }catch(IOException e){
                throw new Error("Error on RuneCut; Icon File not found");
            }
        }else{
             setImageComposer("runeCutTemplate",null);
        }
    }

    



    public static void main(String[] args) {
        try {
            // Set System L&F
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.setLookAndFeel(new FlatDarkLaf());
        } 
        catch (UnsupportedLookAndFeelException e) {
        // handle exception
        }
        /*catch (ClassNotFoundException e) {
        // handle exception
        }
        catch (InstantiationException e) {
        // handle exception
        }
        catch (IllegalAccessException e) {
        // handle exception
        }*/

        //run ui thread
        SwingUtilities.invokeLater(CardDesignerGUI::new);
    }

    
}
