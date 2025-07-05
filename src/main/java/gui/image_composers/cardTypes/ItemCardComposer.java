package gui.image_composers.cardTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import events.CardLoadEvent;
import events.EventBus;
import events.ItemImageUpdateEvent;
import events.RepaintPanelEvent;
import events.TextLoadEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.CardComposer;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class ItemCardComposer extends CardComposer{

    protected BufferedImage cardItemImage;
    protected String cardItemImagePath;
    //protected OneLineTextPane typeTextPane;

    
    

    public String getItemImagePath(){
        return cardItemImagePath;
    }

    public ItemCardComposer(int type){
        super(type);

        
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();

        if (cardItemImage != null) {
            g2d.drawImage(cardItemImage, 0, 0, targetWidth, targetHeight, null);
        }


        g2d.drawImage(drawCardFrame(scale), 0, 0, targetWidth, targetHeight, null);

        BufferedImage typeText = new BufferedImage((int)(typeFieldBounds[2]*scale),  (int)(typeFieldBounds[3]*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = typeText.createGraphics();
        typeTextPane.setSize((int)(typeFieldBounds[2] * scale), (int)(typeFieldBounds[3] * scale));
        typeTextPane.doLayout();
        typeTextPane.validate();
        typeTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(hasTypeBorder){
            typeText = drawStroke(typeText,3,Color.WHITE);
        }
        g2d.drawImage(typeText, (int) (typeFieldBounds[0]*scale), (int)(typeFieldBounds[1]*scale),  (int)(typeFieldBounds[2]*scale),  (int)(typeFieldBounds[3]*scale), null);


        BufferedImage infoText = new BufferedImage((int) (infoFieldBounds[2] * scale),(int) (infoFieldBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        labelGraphics = infoText.createGraphics();
        infoTextPane.setBounds((int) (infoFieldBounds[0] * scale), (int) (infoFieldBounds[1] * scale), (int) (infoFieldBounds[2] * scale),(int) (infoFieldBounds[3] * scale),scale);
        infoTextPane.scaleFont();
        infoTextPane.doLayout();
        infoTextPane.revalidate();
        infoTextPane.repaint(); 
        infoTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(hasInfoBorder){
            infoText = drawStroke(infoText,3,Color.WHITE);
        }
        g2d.drawImage(infoText, (int) (infoFieldBounds[0] * scale), (int) (infoFieldBounds[1] * scale),  (int) (infoFieldBounds[2] * scale),(int) (infoFieldBounds[3] * scale), null);
        
        BufferedImage titleText = new BufferedImage((int) (titleFieldBounds[2] * scale), (int) (titleFieldBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        labelGraphics = titleText.createGraphics();
        titleTextPane.setSize((int) (titleFieldBounds[2] * scale), (int) (titleFieldBounds[3] * scale));   
        titleTextPane.revalidate();
        titleTextPane.repaint(); 
        titleTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(hasTitleBorder){
            titleText = drawStroke(titleText,3,Color.WHITE);
        }
        g2d.drawImage(titleText,(int) (titleFieldBounds[0] * scale), (int) (titleFieldBounds[1] * scale), (int) (titleFieldBounds[2] * scale), (int) (titleFieldBounds[3] * scale), null);
        g2d.dispose();
        return finalImage;
    }


    @Override
    protected void setField(int field, String path){

        switch(field){
            case GlobalVar.ITEM_IMAGE: cardItemImage = getImageFromFile(path); cardItemImagePath = path; EventBus.publish(new RepaintPanelEvent()); break;
            default: super.setField(field, path);
        }

    }

    @Override
    protected void onImageUpdate(ItemImageUpdateEvent e) {

        super.onImageUpdate(e);

        setField(GlobalVar.ITEM_IMAGE, e.path);
        //EventBus.publish(new RepaintPanelEvent());
    }

    @Override
    protected void onLoadCard(CardLoadEvent e) {
        super.onLoadCard(e);
    }

    protected ItemConfig writeToConfig(ItemConfig config){
        super.writeToConfig(config);
        config.cardItemImagePath = cardItemImagePath;

        return config;
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
        for (Field configField : config.getClass().getDeclaredFields()) {
            configField.setAccessible(true);
            try {
                Field targetField = this.getClass().getDeclaredField(configField.getName());
                targetField.setAccessible(true);

                // Check type compatibility before assigning
                if (targetField.getType().isAssignableFrom(configField.getType())) {
                    Object value = configField.get(config);
                    targetField.set(this, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // Field not found in target, or inaccessible — safely skip
            }
        }

        EventBus.publish(new TextLoadEvent(config.titleText, TextLoadEvent.TITLE));
        EventBus.publish(new TextLoadEvent(config.infoText, TextLoadEvent.INFO));
        EventBus.publish(new TextLoadEvent(config.typeText, TextLoadEvent.TYPE));
        //EventBus.publish(new TextLoadEvent(config.titleText, TextLoadEvent.TITLE));
    }

    
}
