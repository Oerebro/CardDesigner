package gui.image_composers.cardTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import events.CardLoadEvent;
import events.EventBus;
import events.ImageUpdate;
import events.RepaintPanelEvent;
import events.TextLoadEvent;
import events.TextUpdate;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.CardComposer;

import java.awt.*;

public class ItemCardComposer extends CardComposer{

    protected BufferedImage cardItemImage;
    protected String cardItemImagePath;

    public String getItemImagePath(){
        return cardItemImagePath;
    }

    public ItemCardComposer(int type){
        super(type);

        
    }

    @Override
    public BufferedImage composeCard(double scale, int type){
        switch(type){
            case GlobalVar.REPAINT_IMAGE:           
                return paintImage(scale);
            case GlobalVar.REPAINT_INFO:
                return paintInfo(scale);
            case GlobalVar.REPAINT_TYPE:
                return paintType(scale);
            case GlobalVar.REPAINT_TITLE:
                return paintTitle(scale);
            case GlobalVar.REPAINT_FRAME:
                return drawCardFrame(scale);
            case GlobalVar.REPAINT_ALL:
                return paintAll(scale);
            default: 
                return super.composeCard(scale, type);
        }
    }

    private BufferedImage paintAll(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = super.composeCard(scale, GlobalVar.REPAINT_ALL);
        Graphics2D g2d = i.createGraphics();
        g2d.drawImage(paintImage(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.drawImage(drawCardFrame(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.drawImage(paintInfo(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.drawImage(paintType(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.drawImage(paintTitle(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return i;
    }

    private BufferedImage paintImage(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = i.createGraphics();
        if (cardItemImage != null) {
            g2d.drawImage(cardItemImage, 0, 0, targetWidth, targetHeight, null);
        }
        g2d.drawImage(drawCardFrame(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return i;
    }

    private BufferedImage paintInfo(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = i.createGraphics();

        BufferedImage infoText = new BufferedImage((int) (infoFieldBounds[2] * scale), (int) (infoFieldBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = infoText.createGraphics();
        infoTextPane.setBounds((int) (infoFieldBounds[0] * scale), (int) (infoFieldBounds[1] * scale), (int) (infoFieldBounds[2] * scale),(int) (infoFieldBounds[3] * scale),scale);
        infoTextPane.scaleFont(scale);
        infoTextPane.doLayout();
        infoTextPane.revalidate();
        infoTextPane.repaint(); 
        infoTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(hasInfoBorder){
            infoText = drawStroke(infoText,(int)(3*scale),Color.WHITE);
        }
        g2d.drawImage(infoText, (int) (infoFieldBounds[0] * scale), (int) (infoFieldBounds[1] * scale),  (int) (infoFieldBounds[2] * scale),(int) (infoFieldBounds[3] * scale), null);
        g2d.dispose();
        return i;
    }

    private BufferedImage paintTitle(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = i.createGraphics();

        BufferedImage titleText = new BufferedImage((int) (titleFieldBounds[2] * scale), (int) (titleFieldBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = titleText.createGraphics();

        titleTextPane.setSize((int) (titleFieldBounds[2] * scale), (int) (titleFieldBounds[3] * scale));   
        titleTextPane.revalidate();
        titleTextPane.repaint(); 
        titleTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(hasTitleBorder){
            titleText = drawStroke(titleText,(int)(3*scale),Color.WHITE);
        }
        g2d.drawImage(titleText,(int) (titleFieldBounds[0] * scale), (int) (titleFieldBounds[1] * scale), (int) (titleFieldBounds[2] * scale), (int) (titleFieldBounds[3] * scale), null);
        g2d.dispose();
        return i;
    }

    private BufferedImage paintType(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = i.createGraphics();

        BufferedImage typeText = new BufferedImage((int)(typeFieldBounds[2]*scale),  (int)(typeFieldBounds[3]*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = typeText.createGraphics();

        typeTextPane.setSize((int)(typeFieldBounds[2] * scale), (int)(typeFieldBounds[3] * scale));
        typeTextPane.doLayout();
        typeTextPane.validate();
        typeTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(hasTypeBorder){
            typeText = drawStroke(typeText,(int)(3*scale),Color.WHITE);
        }
        g2d.drawImage(typeText, (int) (typeFieldBounds[0]*scale), (int)(typeFieldBounds[1]*scale),  (int)(typeFieldBounds[2]*scale),  (int)(typeFieldBounds[3]*scale), null);
        g2d.dispose();
        return i;
    }


    @Override
    protected void setField(int field, String path){

        switch(field){
            case GlobalVar.ITEM_IMAGE: 
                cardItemImage = getImageFromFile(path); 
                cardItemImagePath = path; 
                overlayImage = cardItemImage; 
                overlayPath = getOverlayImagePath(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_IMAGE)); 
                break;
            default: super.setField(field, path);
        }

    }

    @Override
    protected void onImageUpdate(ImageUpdate e) {
        super.onImageUpdate(e);
        setField(GlobalVar.ITEM_IMAGE, e.path);
    }

    protected void loadCard(CardLoadEvent e) {
        EventBus.publish(new TextUpdate(GlobalVar.TITLE_TEXT_UPDATE,e.titleText));
        EventBus.publish(new TextUpdate(GlobalVar.INFO_TEXT_UPDATE,e.infoText));
        super.loadCard(e);
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

                if (targetField.getType().isAssignableFrom(configField.getType())) {
                    Object value = configField.get(config);
                    targetField.set(this, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }

        EventBus.publish(new TextLoadEvent(config.titleText, TextLoadEvent.TITLE));
        EventBus.publish(new TextLoadEvent(config.infoText, TextLoadEvent.INFO));
        EventBus.publish(new TextLoadEvent(config.typeText, TextLoadEvent.TYPE));
    }

    
}
