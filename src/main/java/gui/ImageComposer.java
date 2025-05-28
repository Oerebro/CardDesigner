package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import events.EventBus;
import events.ImageUpdateEvent;
import events.RepaintPanelEvent;
import events.CardLoadEvent;
import events.ClearUnrelatedImagesEvent;

import java.awt.*;

public class ImageComposer {
    private BufferedImage cardFrame, cardBackground, cardTextbox, cardTitle,cardItemImage, attributeImage,cardType,handedImage, tierGlyph, weaponType, runeSlot,runeCut,armorclass1,armorclass2,effectImage;

    public ImageComposer(){
        EventBus.subscribe(ClearUnrelatedImagesEvent.class, this::onClearUnrelatedImages);
        EventBus.subscribe(ImageUpdateEvent.class, this::onImageUpdate);
        EventBus.subscribe(CardLoadEvent.class, this::onLoadCard);
        
    }

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

        if (effectImage != null) {
            g2d.drawImage(effectImage, 0, 0, targetWidth, targetHeight, null);
        }

        if (cardFrame != null) {
            g2d.drawImage(cardFrame, 0, 0, targetWidth, targetHeight, null);
        }

        /*try
        {
            g2d.drawImage(ImageIO.read(new File("resources/misc/frontborder.png")), 0, 0, targetWidth, targetHeight, null);
        }catch(IOException ignore){}*/

        if (cardItemImage != null) {
            g2d.drawImage(cardItemImage, (int)(90*scale), (int)(130*scale),  (int)(570*scale),  (int)(570*scale), null);
        }

        if (cardTextbox != null) {
            g2d.drawImage(cardTextbox, (int)(0*scale), (int)(440*scale), targetWidth, (int)(610*scale), null);
        }

        if (cardType != null) {
            g2d.drawImage(cardType, (int)(530*scale), (int)(490*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(465*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        if (weaponType != null) {
            g2d.drawImage(weaponType, (int) (30*scale), (int)(435*scale), (int) (213*scale), (int) (199*scale), null);
        }

        if (attributeImage != null) {
            g2d.drawImage(attributeImage, (int) (60*scale), (int)(435*scale), (int) (140*scale), (int) (140*scale), null);
        }

        if (runeSlot != null) {
            g2d.drawImage(runeSlot, (int) (0*scale), (int)(0*scale), (int) (750*scale), (int) (1050*scale), null);
        }

        if (cardTitle != null) {
            g2d.drawImage(cardTitle, 0, (int)(10*scale), targetWidth, targetHeight, null);
        }

        if (runeCut != null) {
            g2d.drawImage(runeCut, 0, 0, targetWidth, targetHeight, null);
        }

        if (armorclass1 != null) {
            g2d.drawImage(armorclass1, (int)(570*scale), (int)(545*scale), (int)(100*scale), (int)(100*scale), null);
        }

        if (armorclass2 != null) {
            g2d.drawImage(armorclass2, (int)(570*scale), (int)(545*scale), (int)(100*scale), (int)(100*scale), null);
        }


        return finalImage;
    }

    public void onClearUnrelatedImages(ClearUnrelatedImagesEvent e){
        setField("cardType",null);
        setField("ac1",null);
        setField("ac2",null);
        setField("cardItemImage",null);
        setField("runeSlot",null);
        setField("weaponType",null);
        setField("attributeImage",null);
        setField("effectImage",null);
    }

    public void onImageUpdate(ImageUpdateEvent e){
        setField(e.type,e.path);
    }

    public void onLoadCard(CardLoadEvent e){
            cardFrame = getImageFromFile(e.frameImage) ;
            cardBackground = getImageFromFile(e.backgroundImage) ;
            cardTextbox = getImageFromFile(e.textboxImage) ;
            cardTitle = getImageFromFile(e.titleImage) ;
    }


    private void setField(String field, String path){
        switch(field){
            case "cardFrame": cardFrame = getImageFromFile(path) ;break;
            case "cardBackground": cardBackground = getImageFromFile(path) ;break;
            case "cardTextbox": cardTextbox = getImageFromFile(path) ;break;
            case "cardTitle": cardTitle = getImageFromFile(path) ;break;
            case "cardItemImage": cardItemImage = getImageFromFile(path) ;break;
            case "attributeImage": attributeImage = getImageFromFile(path) ;break;
            case "cardType": cardType = getImageFromFile(path) ;System.out.println(path);break;
            case "handedImage": handedImage = getImageFromFile(path) ;break;
            case "tierGlyph": tierGlyph = getImageFromFile(path) ;break;
            case "runeSlot": runeSlot = getImageFromFile(path) ;break;
            case "weaponType": weaponType = getImageFromFile(path) ;break;
            case "runeCutTemplate": runeCut = getImageFromFile(path) ;break;
            case "ac1": armorclass1 = getImageFromFile(path) ;break;
            case "ac2": armorclass2 = getImageFromFile(path) ;break;
            case "effect": effectImage = getImageFromFile(path) ;break;
        }

        EventBus.publish(new RepaintPanelEvent());
    }

    private BufferedImage getImageFromFile(String path){
        if(path == null){
            return null;
        }

        BufferedImage i = null;
         try{
            i = ImageIO.read(new File(path));
        }catch(IOException e){
            throw new Error("Error on ImageComposer::getImageFromFile ("+path+"); File not found");
        }
        return i;
    }

    
}
