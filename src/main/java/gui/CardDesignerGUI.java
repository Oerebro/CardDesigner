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
        imageComposer.setField(field,i);
        previewPanel.repaint();
    }

    public BufferedImage getComposedCard(double scale){
        return imageComposer.composeCard(getFrameScale());
    }

    private void clearUnrelatedFields(){
        setImageComposer("cardType",null);
        setImageComposer("ac1",null);
        setImageComposer("ac2",null);
        setImageComposer("cardItemImage",null);
        setImageComposer("runeSlot",null);
        setImageComposer("weaponType",null);
        setImageComposer("attributeImage",null);
        setImageComposer("effectImage",null);
        updateTier(0);
    }

    public void onButtonWeapon(){
        clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/dice/d6.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Weapon; Icon File not found");
        }
        controlPanel.itemArtChangeToType(0);
        
    }

    public void onButtonRune(){
        clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/glyphs/rune.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Rune; Icon File not found");
        }
        controlPanel.itemArtChangeToType(10);
    }

    public void onButtonEffect(){
        clearUnrelatedFields();
        setImageComposer("cardType",null);
        controlPanel.itemArtChangeToType(5);
    }

    public void onButtonCharacter(){
        clearUnrelatedFields();
        try{
            setImageComposer("character",ImageIO.read(new File("resources/character.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Effects; Icon File not found");
        }
        controlPanel.itemArtChangeToType(6);
    }

    public void onButtonArmor(){
        clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/armor.png")));
            setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_armor.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Armor; Icon File not found");
        }
        if(controlPanel.getItemArtType()!=1){
            controlPanel.itemArtChangeToType(1);
            setImageComposer("cardItemImage",null);
        }
        
    }

    public void onButtonClothing(){
        clearUnrelatedFields();
        try{
           setImageComposer("cardType",ImageIO.read(new File("resources/clothing.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Clothing; Icon File not found");
        }
        if(controlPanel.getItemArtType()!=1){
            controlPanel.itemArtChangeToType(1);
            setImageComposer("cardItemImage",null);
        }
    }

    public void onButtonAccessoire(){
        clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/accessoire.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Accessoire; Icon File not found");
        }
        controlPanel.itemArtChangeToType(3);
        updateTier(0);
    }

    public void onButtonConsumable(){
        clearUnrelatedFields();
        try{
            setImageComposer("cardType",ImageIO.read(new File("resources/consumable.png")));
        }catch(IOException e){
            throw new Error("Error on Checkbox Consumable; Icon File not found");
        }
        controlPanel.itemArtChangeToType(4);
    }

    public void onButtonTwoHanded(boolean selected){
        clearUnrelatedFields();
        try{
            if(selected){
                setImageComposer("handedImage",ImageIO.read(new File("resources/misc/twoHanded.png")));
                controlPanel.itemArtChangeToType(10);
            }else{    
            setImageComposer("handedImage",ImageIO.read(new File("resources/misc/oneHanded.png")));
            }
        }catch(IOException e){
            throw new Error("Couldnt find image resources/misc/*Handed.png");
        }
        
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

    public void updateArmorClass(int ac) {
        
        if(ac == 0){
            setImageComposer("ac1",null);
            setImageComposer("ac2",null);
            return;
        }

        if(ac < 10){
            try{
                setImageComposer("ac1",ImageIO.read(new File("resources/glyphs/ac/"+ac+".png")));
                setImageComposer("ac2", null);
            }catch(IOException e){
                throw new Error("Error on AC; Icon File not found");
            }
            return;
        }else if(ac >=10 && ac < 20){
            try{
                setImageComposer("ac1",ImageIO.read(new File("resources/glyphs/ac/1_.png")));
                ac %= 10;
                setImageComposer("ac2",ImageIO.read(new File("resources/glyphs/ac/_2.png")));
            }catch(IOException e){
                throw new Error("Error on AC; Icon File not found");
            }
            return;
        }else if(ac >=20 && ac < 30){
            try{
                setImageComposer("ac1",ImageIO.read(new File("resources/glyphs/ac/2_.png")));
                ac %= 10;
                setImageComposer("ac2",ImageIO.read(new File("resources/glyphs/ac/_"+ac+".png")));
            }catch(IOException e){
                throw new Error("Error on AC; Icon File not found");
            }
        }else if(ac >=30){
            try{
                setImageComposer("ac1",ImageIO.read(new File("resources/glyphs/ac/3_.png")));
                ac %= 10;
                setImageComposer("ac2",ImageIO.read(new File("resources/glyphs/ac/_"+ac+".png")));
            }catch(IOException e){
                throw new Error("Error on AC; Icon File not found");
            }
        }

    }

    public void updateRange(String str, Font font) {
        previewPanel.updateRangeText(str, font);
    }

    public void updateDice(int num){
        switch (num){
            case 0: setImageComposer("cardType",null);break;
            case 4: try{
                        setImageComposer("cardType",ImageIO.read(new File("resources/dice/d4.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 6: try{
                        setImageComposer("cardType",ImageIO.read(new File("resources/dice/d6.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 8: try{
                        setImageComposer("cardType",ImageIO.read(new File("resources/dice/d8.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 10: try{
                        setImageComposer("cardType",ImageIO.read(new File("resources/dice/d10.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 12: try{
                        setImageComposer("cardType",ImageIO.read(new File("resources/dice/d12.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
        }

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

    public void updateWeaponType(String type){
        switch (type){
            case "none": setImageComposer("weaponType",null);break;
            case "ranged": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_ranged.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "melee": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_melee.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "throwable": try{
                        setImageComposer("weaponType",ImageIO.read(new File("resources/glyphs/ui_throwable.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
        }
    }

    public void updateAttributeImage(String type){
        switch (type){
            case "str": try{
                        setImageComposer("attributeImage",ImageIO.read(new File("resources/glyphs/stat_strength.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "con": try{
                        setImageComposer("attributeImage",ImageIO.read(new File("resources/glyphs/stat_constitution.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "dex": try{
                        setImageComposer("attributeImage",ImageIO.read(new File("resources/glyphs/stat_dexterity.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "intel": try{
                        setImageComposer("attributeImage",ImageIO.read(new File("resources/glyphs/stat_intelligence.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "wis": try{
                        setImageComposer("attributeImage",ImageIO.read(new File("resources/glyphs/stat_wisdom.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
            case "rizz": try{
                        setImageComposer("attributeImage",ImageIO.read(new File("resources/glyphs/stat_charisma.png")));
                    }catch(IOException e){
                        throw new Error("Error on Weapon Type; Icon File not found");
                    }break;
        }
    }

    public void updateTier(int num){
        switch (num){
            case 0: setImageComposer("tierGlyph",null);break;
            case 1: try{
                        setImageComposer("tierGlyph",ImageIO.read(new File("resources/glyphs/tier1.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 2: try{
                        setImageComposer("tierGlyph",ImageIO.read(new File("resources/glyphs/tier2.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 3: try{
                        setImageComposer("tierGlyph",ImageIO.read(new File("resources/glyphs/tier3.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
            case 4: try{
                        setImageComposer("tierGlyph",ImageIO.read(new File("resources/glyphs/tier4.png")));
                    }catch(IOException e){
                        throw new Error("Error on Dice; Icon File not found");
                    }break;
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
