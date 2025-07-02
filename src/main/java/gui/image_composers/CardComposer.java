package gui.image_composers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import events.EventBus;
import events.ItemImageUpdateEvent;
import events.InfoColorUpdate;
import events.InfoTextUpdate;
import events.RepaintPanelEvent;
import events.TextLoadEvent;
import events.TitleTextUpdate;
import events.ToggleTitleBorder;
import events.CardLoadEvent;

import java.awt.*;

import gui.GlobalVar;
import gui.Loggable;
import gui.card_types.*;
import gui.previewpanel.JScalingTextPane;
import gui.previewpanel.OneLineTextPane;

public class CardComposer extends Loggable{
    protected int type;
    protected BufferedImage cardFrame, cardBackground,cardType,runeCut,overlayImage, cardTextBox, cardTitle, cardCrown;
    protected String cardFramePath, cardBackgroundPath,cardTypePath,overlayPath,runeCutPath, cardTextBoxPath, cardTitlePath, cardCrownPath;
    protected JScalingTextPane infoTextPane;
    protected OneLineTextPane titleTextPane,typeTextPane;
    protected Boolean hasTitleBorder, hasRuneCut;

    protected String TYPEIMAGEPATH = "reconfigs/glyphs/";
    protected String ARMOR_TYPEIMAGE = "armor.png";
    protected String ACCESSOIRE_TYPEIMAGE = "accessoire.png";
    protected String CONSUMABLE_TYPEIMAGE = "consumable.png";
    protected String CLOTHING_TYPEIMAGE = "clothing.png";

    protected String W_MELEE_TYPEIMAGE = "weapon_type/melee.png";
    protected String W_RANGED_TYPEIMAGE = "weapon_type/ranged.png";
    protected String W_THROWABLE_TYPEIMAGE = "weapon_type/throwable.png";

    protected String RUNECUT = "reconfigs/misc/rune_cut.png";

    protected int baseWidth = 750;
    protected int baseHeight = 1050;

    protected int targetWidth, targetHeight;

    protected int titleX,titleY, titleW, titleH;
    protected int infoX,infoY, infoW, infoH;

    protected boolean titleBorder;


    public CardComposer(int type){

        titleX = 40;
        titleY = 50;
        titleW = 670;
        titleH = 50;

        infoX = 55;
        infoY = 665;
        infoW = 640;
        infoH = 295;

        this.type = type;
        log("type" + type);
        
        titleBorder = false;

        

        infoTextPane = new JScalingTextPane(9, 72);
        infoTextPane.setBounds(infoX,infoY,infoW,infoH);
        infoTextPane.setSize(infoW,infoH);


        titleTextPane = new OneLineTextPane(OneLineTextPane.TITLE, 200, titleX,titleY,titleW,titleH);
        titleTextPane.setForeground(Color.WHITE);

        //titleTextPane.setBounds(80,20,590,80);
        //titleTextPane.setBounds(0,100,590,80);
        
        init();

        EventBus.subscribe(ItemImageUpdateEvent.class, this::onImageUpdate);
        EventBus.subscribe(CardLoadEvent.class, this::onLoadCard);
        EventBus.subscribe(InfoTextUpdate.class, this::onInfoTextUpdate);
        EventBus.subscribe(InfoColorUpdate.class, this::onInfoColorUpdate);
        EventBus.subscribe(ToggleTitleBorder.class, this::toggleTitleBorder);

    }

    protected void onInfoColorUpdate(InfoColorUpdate e){
        infoTextPane.setColor(e.color);
    }

    protected void init(){};

    protected void toggleTitleBorder(ToggleTitleBorder e){
        titleBorder = e.bool;
        EventBus.publish(new RepaintPanelEvent());
    }

    public BufferedImage composeCard(double scale){
        int targetWidth = (int) (baseWidth * scale);
        int targetHeight = (int) (baseHeight * scale);

        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        if (cardBackground != null) {
            g2d.drawImage(cardBackground, 0, 0, targetWidth, targetHeight, null);
        }

        if (cardType != null) {
            g2d.drawImage(cardType, (int)(530*scale), (int)(490*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        
        g2d.dispose();
        return finalImage;
    }


    protected void onImageUpdate(ItemImageUpdateEvent e){
    }

    protected void onLoadCard(CardLoadEvent e){
        //this.type=e.type;
        EventBus.publish(new TitleTextUpdate(e.titleText));
        EventBus.publish(new InfoTextUpdate(e.infoText));
        setField(GlobalVar.BACKGROUND_IMAGE, e.backgroundImage);
        setField(GlobalVar.FRAME_IMAGE, e.frameImage);

        setField(GlobalVar.TITLE_IMAGE, e.titleImage);
        setField(GlobalVar.CROWN_IMAGE, e.crownImage);
        setField(GlobalVar.TEXTBOX_IMAGE, e.textBoxImage);

    }

    protected void onInfoTextUpdate(InfoTextUpdate e){
        EventBus.publish(new RepaintPanelEvent());
    }


    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.BACKGROUND_IMAGE: cardBackground = getImageFromFile(path); cardBackgroundPath = path;break;
            case GlobalVar.FRAME_IMAGE: cardFrame = getImageFromFile(path); cardFramePath = path;break;
            case GlobalVar.TITLE_IMAGE: cardTitle = getImageFromFile(path); cardTitlePath = path;break;
            case GlobalVar.CROWN_IMAGE: cardCrown = getImageFromFile(path); cardCrownPath = path;break;
            case GlobalVar.TEXTBOX_IMAGE: cardTextBox = getImageFromFile(path); cardTextBoxPath = path;break;
        }

        EventBus.publish(new RepaintPanelEvent());
    }

    protected String getOverlayImagePath(String path){
        String[] arr = path.split("\\\\");
        return "reconfigs/img/overlay/"+arr[arr.length-1];
    }


    protected BufferedImage getImageFromFile(String path){
        if(path == null){
            return null;
        }

        BufferedImage i = null;
         try{
            i = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("Error on ImageComposer::getImageFromFile ("+path+"); File not found");
            return null;
     
        }
        return i;
    }

    protected BufferedImage drawStroke(BufferedImage src, int strokeWidth, Color color) {
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

    protected BufferedImage tintAlpha(BufferedImage src, Color color) {
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

    protected BufferedImage drawCardFrame(double scale){

        int[] titleBounds = {20,20,710,200};
        int[] textboxBounds = {0,560,750,450};
        

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = finalImage.createGraphics();

        if (cardFrame != null) {
            g2d.drawImage(cardFrame, 0, 0, targetWidth, targetHeight, null);
        }

        if(cardTitle != null){
            g2d.drawImage(cardTitle, (int)(titleBounds[0]*scale), (int)(titleBounds[1]*scale), (int)(titleBounds[2]*scale), (int)(titleBounds[3]*scale), null);
        }

        if (cardCrown != null) {
            g2d.drawImage(cardCrown, (int)(titleBounds[0]*scale), (int)(titleBounds[1]*scale), (int)(titleBounds[2]*scale), (int)(titleBounds[3]*scale), null);
        }

        if (cardTextBox != null) {
            g2d.drawImage(cardTextBox, (int)(textboxBounds[0]*scale), (int)(textboxBounds[1]*scale), (int)(textboxBounds[2]*scale), (int)(textboxBounds[3]*scale), null);
        }

        if (runeCut != null) {
            g2d.drawImage(runeCut, 0, 0, targetWidth, targetHeight, null);
        }

        return finalImage;
    }

    protected CardConfig writeToConfig(CardConfig config){
        log("write type "+type);
        config.cardFramePath = cardFramePath;
        config.cardBackgroundPath = cardBackgroundPath;
        config.cardTitlePath = cardTitlePath;
        config.cardCrownPath = cardCrownPath;
        config.cardTextBoxPath = cardTextBoxPath;
        config.titleText = titleTextPane.getText();
        config.typeText = typeTextPane.getText();
        config.type = this.type;
        //config.hasTitleBorder = hasTitleBorder;

        return config;
    }

    public void loadFromConfig(CardConfig config) {
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

    public CardConfig saveConfig(){return null;};
    public CardConfig loadConfig(){return null;};
    
}
