package gui;

import java.awt.image.BufferedImage;

import java.awt.*;

public class ImageComposer {
    private BufferedImage cardFrame, cardBackground, cardTextbox, cardTitle,cardItemImage, attributeImage,cardType,handedImage, tierGlyph, weaponType, runeSlot,runeCut,armorclass1,armorclass2;

    public BufferedImage getTier(){
        return tierGlyph;
    }

    public BufferedImage getCardBackground(){
        return cardBackground;
    }
    public BufferedImage getCardTextbox(){
        return cardTextbox;
    }
    public BufferedImage getCardFrame(){
        return cardFrame;
    }
    public BufferedImage getCardItemImage(){
        return cardItemImage;
    }

    public BufferedImage getCardHandedImage(){
        return handedImage;
    }

    public BufferedImage getWeaponType(){
        return weaponType;
    }

    public BufferedImage getAttributeImage(){
        return attributeImage;
    }

    public BufferedImage composeCard(double scale){
        int targetWidth = (int)(750 * scale);
        int targetHeight = (int)(1050 * scale);

        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cardBackground != null) {
            g2d.drawImage(cardBackground, 0, 0, targetWidth, targetHeight, null);
        }

        if (cardFrame != null) {
            g2d.drawImage(cardFrame, 0, 0, targetWidth, targetHeight, null);
        }

        /*try
        {
            g2d.drawImage(ImageIO.read(new File("resources/misc/frontborder.png")), 0, 0, targetWidth, targetHeight, null);
        }catch(IOException ignore){}*/

        if (cardItemImage != null) {
            g2d.drawImage(cardItemImage, (int)(120*scale), (int)(130*scale),  (int)(510*scale),  (int)(510*scale), null);
        }

        if (cardTextbox != null) {
            g2d.drawImage(cardTextbox, (int)(0*scale), (int)(440*scale), targetWidth, (int)(610*scale), null);
        }

        if (cardType != null) {
            g2d.drawImage(cardType, (int)(530*scale), (int)(465*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(465*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        if (weaponType != null) {
            g2d.drawImage(weaponType, (int) (30*scale), (int)(410*scale), (int) (213*scale), (int) (199*scale), null);
        }

        if (attributeImage != null) {
            g2d.drawImage(attributeImage, (int) (60*scale), (int)(410*scale), (int) (140*scale), (int) (140*scale), null);
        }

        if (runeSlot != null) {
            g2d.drawImage(runeSlot, (int) (0*scale), (int)(0*scale), (int) (750*scale), (int) (1050*scale), null);
        }

        if (cardTitle != null) {
            g2d.drawImage(cardTitle, 0, (int)(10*scale), targetWidth, targetHeight, null);
        }

        if (runeCut != null) {
            g2d.drawImage(runeCut, 0, (int)(440*scale), targetWidth, (int)(610*scale), null);
        }

        if (armorclass1 != null) {
            g2d.drawImage(armorclass1, (int)(560*scale), (int)(490*scale), (int)(120*scale), (int)(120*scale), null);
        }

        if (armorclass2 != null) {
            g2d.drawImage(armorclass1, (int)(560*scale), (int)(490*scale), (int)(120*scale), (int)(120*scale), null);
        }


        return finalImage;
    }

    public void setField(String field, BufferedImage i){
        switch(field){
            case "cardFrame": cardFrame = i ;break;
            case "cardBackground": cardBackground = i ;break;
            case "cardTextbox": cardTextbox = i ;break;
            case "cardTitle": cardTitle = i ;break;
            case "cardItemImage": cardItemImage = i ;break;
            case "attributeImage": attributeImage = i ;break;
            case "cardType": cardType = i ;break;
            case "handedImage": handedImage = i ;break;
            case "tierGlyph": tierGlyph = i ;break;
            case "runeSlot": runeSlot = i ;break;
            case "weaponType": weaponType = i ;break;
            case "runeCutTemplate": runeCut = i ;break;
            case "ac1": armorclass1 = i ;break;
            case "ac2": armorclass2 = i ;break;
        }
    }

    
}
