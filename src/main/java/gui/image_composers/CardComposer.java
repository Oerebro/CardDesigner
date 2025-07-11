package gui.image_composers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;


import events.EventBus;
import events.ImageUpdate;
import events.InfoColorUpdate;
import events.RepaintPanelEvent;
import events.TextLoadEvent;
import events.TextUpdate;
import events.ToggleTextBorder;
import events.CardLoadEvent;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import gui.GlobalVar;
import gui.Loggable;
import gui.card_types.*;
import gui.previewpanel.JScalingTextPane;
import gui.previewpanel.OneLineTextPane;
import java.awt.geom.Area;

public class CardComposer extends Loggable{
    protected int type;
    protected BufferedImage cardFrame, cardBackground,cardType,runeCut,overlayImage, cardTextBox, cardTitle, cardCrown;
    protected String cardFramePath, cardBackgroundPath,cardTypePath,overlayPath,runeCutPath, cardTextBoxPath, cardTitlePath, cardCrownPath;
    protected JScalingTextPane infoTextPane;
    protected OneLineTextPane titleTextPane,typeTextPane;
    protected Boolean hasTitleBorder, hasRuneCut, hasInfoBorder, hasTypeBorder;

    protected String TYPEIMAGEPATH = "resources/glyphs/";
    protected String ARMOR_TYPEIMAGE = "armor.png";
    protected String ACCESSOIRE_TYPEIMAGE = "accessoire.png";
    protected String CONSUMABLE_TYPEIMAGE = "consumable.png";
    protected String CLOTHING_TYPEIMAGE = "clothing.png";

    protected String W_MELEE_TYPEIMAGE = "weapon_type/melee.png";
    protected String W_RANGED_TYPEIMAGE = "weapon_type/ranged.png";
    protected String W_THROWABLE_TYPEIMAGE = "weapon_type/throwable.png";

    protected String RUNECUT = "resources/glyphs/runic_weapon.png";

    protected int baseWidth = 750;
    protected int baseHeight = 1050;

    protected int targetWidth, targetHeight;

    protected int[] typeFieldBounds = {50,605,650,40};
    protected int[] titleFieldBounds = {57,70,632,60};
    protected int[] infoFieldBounds = {55,665,640,295};
    protected int[] runeIconBounds = {40,40,90,90};

    //protected boolean hasTitleBorder;


    public CardComposer(int type){

        this.type = type;
        
        hasTitleBorder = true;
        hasTypeBorder = true;
        hasInfoBorder = true;

        

        infoTextPane = new JScalingTextPane(9, 72);
        infoTextPane.setBounds(infoFieldBounds[0],infoFieldBounds[1],infoFieldBounds[2],infoFieldBounds[3]);
        infoTextPane.setSize(infoFieldBounds[2],infoFieldBounds[3]);
        infoTextPane.setColor(Color.BLACK);


        titleTextPane = new OneLineTextPane(GlobalVar.TITLE_TEXT_UPDATE, 200, titleFieldBounds[0],titleFieldBounds[1],titleFieldBounds[2],titleFieldBounds[3]);
        titleTextPane.setForeground(Color.BLACK);

        typeTextPane = new OneLineTextPane(GlobalVar.TYPE_TEXT_UPDATE, 200, typeFieldBounds[0],typeFieldBounds[1],typeFieldBounds[2],typeFieldBounds[3]);
        typeTextPane.setOpaque(false);
        typeTextPane.setForeground(Color.BLACK);

        //titleTextPane.setBounds(80,20,590,80);
        //titleTextPane.setBounds(0,100,590,80);
        
        init();

        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
        //EventBus.subscribe(CardLoadEvent.class, this::onLoadCard);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        EventBus.subscribe(InfoColorUpdate.class, this::onInfoColorUpdate);
        EventBus.subscribe(ToggleTextBorder.class, this::toggleTextBorder);
        

    }

    protected void onInfoColorUpdate(InfoColorUpdate e){
        infoTextPane.setColor(e.color);
    }

    protected void init(){};

    protected void toggleTextBorder(ToggleTextBorder e){
        switch(e.type){
            case    GlobalVar.TITLE_BORDER: hasTitleBorder = e.bool; 
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE)); 
                    break;
            case    GlobalVar.INFO_BORDER: hasInfoBorder = e.bool; 
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE)); 
                    break;
            case    GlobalVar.TYPE_BORDER: hasTypeBorder = e.bool;
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE)); 
                    break;
        }
        
    }

    public BufferedImage composeCard(double scale, int type){
        int targetWidth = (int) (baseWidth * scale);
        int targetHeight = (int) (baseHeight * scale);

        BufferedImage finalImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);

        if (cardBackground != null) {
            System.out.println("Backgroundupdate");
            g2d.drawImage(cardBackground, 0, 0, targetWidth, targetHeight, null);
        }

        g2d.drawImage(drawCardFrame(scale), 0, 0, targetWidth, targetHeight, null);

        /*if (cardType != null) {
            g2d.drawImage(cardType, (int)(530*scale), (int)(490*scale),  (int)(180*scale),  (int)(180*scale), null);
        }*/

        
        g2d.dispose();
        return finalImage;
    }

    protected void onImageUpdate(ImageUpdate e){
    }

    protected void loadCard(CardLoadEvent e){
        //this.type=e.type;
        
        setField(GlobalVar.BACKGROUND_IMAGE, e.backgroundImage);
        setField(GlobalVar.FRAME_IMAGE, e.frameImage);

        setField(GlobalVar.TITLE_IMAGE, e.titleImage);
        setField(GlobalVar.CROWN_IMAGE, e.crownImage);
        setField(GlobalVar.TEXTBOX_IMAGE, e.textBoxImage);

    }

    protected void onTextUpdate(TextUpdate e){
        switch(e.type){
            case    GlobalVar.INFO_TEXT_UPDATE:
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE)); 
                    break;
            case    GlobalVar.TITLE_TEXT_UPDATE:; 
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE)); 
                    break;
            case    GlobalVar.TYPE_TEXT_UPDATE:
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TITLE)); 
                    break;
        }
    }


    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.BACKGROUND_IMAGE: cardBackground = getImageFromFile(path);cardBackgroundPath = path;break;
            case GlobalVar.FRAME_IMAGE: cardFrame = getImageFromFile(path); cardFramePath = path;break;
            case GlobalVar.TITLE_IMAGE: cardTitle = getImageFromFile(path); cardTitlePath = path;break;
            case GlobalVar.CROWN_IMAGE: cardCrown = getImageFromFile(path); cardCrownPath = path;break;
            case GlobalVar.TEXTBOX_IMAGE: cardTextBox = getImageFromFile(path); cardTextBoxPath = path;break;
        }

        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_BACKGROUND));
    }

    protected String getOverlayImagePath(String path){
        String[] arr = path.split("\\\\");
        return "resources/img/overlay/"+arr[arr.length-1];
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

        BufferedImage result = new BufferedImage(w+(strokeWidth*2), h+(strokeWidth*2), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.SrcOver);

        // Draw offset copies to simulate a soft stroke (cheap blur)
        for (int dx = -strokeWidth; dx <= strokeWidth; dx++) {
            for (int dy = -strokeWidth; dy <= strokeWidth; dy++) {
                if (dx * dx + dy * dy <= strokeWidth * strokeWidth) {
                    g.drawImage(tintAlpha(src, color), dx+strokeWidth, dy+strokeWidth, null);
                }
            }
        }

        // Draw the original image on top
        g.drawImage(src, strokeWidth, strokeWidth, null);
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
        //int[] runeIconBounds = {40,40,90,90};
        int[] runeIconBounds = {635,935,90,90};
        

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
            g2d.drawImage(runeCut, (int)(runeIconBounds[0]*scale), (int)(runeIconBounds[1]*scale), (int)(runeIconBounds[2]*scale), (int)(runeIconBounds[3]*scale), null);
        }
        //return finalImage;
        return paintWhiteCorners(finalImage);
    }

    protected CardConfig writeToConfig(CardConfig config){
        config.cardFramePath = cardFramePath;
        config.cardBackgroundPath = cardBackgroundPath;
        config.cardTitlePath = cardTitlePath;
        config.cardCrownPath = cardCrownPath;
        config.cardTextBoxPath = cardTextBoxPath;
        config.titleText = titleTextPane.getText();
        config.typeText = typeTextPane.getText();
        config.type = this.type;
        config.hasTitleBorder = hasTitleBorder;

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

    public static BufferedImage paintWhiteCorners(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();

        double realWidthMM = 66.0;
        double realHeightMM = 88.0;

        // Calculate pixels per mm (average)
        double pxPerMM_X = width / realWidthMM;
        double pxPerMM_Y = height / realHeightMM;
        double pxPerMM = (pxPerMM_X + pxPerMM_Y) / 2.0;

        int r = (int) Math.round(3.0 * pxPerMM);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        try {
            g2.drawImage(input, 0, 0, null);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);

            // Helper to create concave corner shape
            // A square minus a quarter circle ellipse in the corner

            // Top-left corner
            Area tl = new Area(new Rectangle(0, 0, r, r));
            Shape tlCircle = new Ellipse2D.Double(0, 0, 2 * r, 2 * r);
            tl.subtract(new Area(tlCircle));
            g2.fill(tl);

            // Top-right corner
            Area tr = new Area(new Rectangle(width - r, 0, r, r));
            Shape trCircle = new Ellipse2D.Double(width - 2 * r, 0, 2 * r, 2 * r);
            tr.subtract(new Area(trCircle));
            g2.fill(tr);

            // Bottom-right corner
            Area br = new Area(new Rectangle(width - r, height - r, r, r));
            Shape brCircle = new Ellipse2D.Double(width - 2 * r, height - 2 * r, 2 * r, 2 * r);
            br.subtract(new Area(brCircle));
            g2.fill(br);

            // Bottom-left corner
            Area bl = new Area(new Rectangle(0, height - r, r, r));
            Shape blCircle = new Ellipse2D.Double(0, height - 2 * r, 2 * r, 2 * r);
            bl.subtract(new Area(blCircle));
            g2.fill(bl);

        } finally {
            g2.dispose();
        }

        return output;
    }
}
