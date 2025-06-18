package gui.image_composers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;

import events.EventBus;
import events.ItemImageUpdateEvent;
import events.InfoColorUpdate;
import events.InfoTextUpdate;
import events.RepaintPanelEvent;
import events.TitleTextUpdate;
import events.ToggleTitleBorder;
import events.CardLoadEvent;

import java.awt.*;

import gui.card_types.*;
import gui.previewpanel.JScalingTextPane;
import gui.previewpanel.OneLineTextPane;

public class CardComposer {
    protected int type;
    protected BufferedImage cardFrame, cardBackground,cardType,runeCut,overlayImage;
    protected String cardFramePath, cardBackgroundPath,cardTypePath,overlayPath,runeCutPath;
    protected JScalingTextPane infoTextPane;
    protected OneLineTextPane titleTextPane;
    protected Boolean titleBorder;

    protected String TYPEIMAGEPATH = "resources/glyphs/";
    protected String ARMOR_TYPEIMAGE = "armor.png";
    protected String ACCESSOIRE_TYPEIMAGE = "accessoire.png";
    protected String CONSUMABLE_TYPEIMAGE = "consumable.png";
    protected String CLOTHING_TYPEIMAGE = "clothing.png";

    protected String W_MELEE_TYPEIMAGE = "weapon_type/melee.png";
    protected String W_RANGED_TYPEIMAGE = "weapon_type/ranged.png";
    protected String W_THROWABLE_TYPEIMAGE = "weapon_type/throwable.png";

    protected String RUNECUT = "resources/misc/rune_cut.png";

    protected int baseWidth = 750;
    protected int baseHeight = 1050;

    protected int targetWidth, targetHeight;

    protected int titleX,titleY, titleW, titleH;
    protected int infoX,infoY, infoW, infoH;


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

        

        return finalImage;
    }


    protected void onImageUpdate(ItemImageUpdateEvent e){
    }

    protected void onLoadCard(CardLoadEvent e){
        this.type=e.type;
        EventBus.publish(new TitleTextUpdate(e.titleText));
        EventBus.publish(new InfoTextUpdate(e.infoText));
        setField(Card.BACKGROUND_IMAGE, e.backgroundImage);
        setField(Card.FRAME_IMAGE, e.frameImage);

    }

    protected void onInfoTextUpdate(InfoTextUpdate e){
        EventBus.publish(new RepaintPanelEvent());
    }


    protected void setField(int field, String path){
        switch(field){
            case Card.BACKGROUND_IMAGE: cardBackground = getImageFromFile(path); cardBackgroundPath = path;break;
            case Card.FRAME_IMAGE: cardFrame = getImageFromFile(path); cardFramePath = path;break;
        }
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

    public void saveConfig(File file) {
        CardConfig config = new CardConfig();
        switch(type){
            case Card.CHARACTER:  config = new CharacterConfig(); break;
            case Card.EFFECT: config = new EffectConfig(); break;
            case Card.WEAPON: config = new WeaponConfig(); break;
            case Card.ARMOR: config = new ArmorConfig(); break;
            case Card.CONSUMABLE: config = new ConsumableConfig();break;
            case Card.RUNE: config = new RuneConfig(); break;
            case Card.ACCESSOIRE: config = new AccessoireConfig(); break;
        }
        

        config.cardFrame = cardFramePath;
        config.cardBackground = cardBackgroundPath;
        config.type = type;

        config.infoText = infoTextPane.getText();
        config.titleText = titleTextPane.getText();
        config.hasTitleBorder = titleBorder;


        //EventBus.publish(new GetCardAttributesEvent(config));

        try {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CardConfig config = mapper.readValue(file, CardConfig.class);

            switch(config.type){
                case Card.CHARACTER:  config = new CharacterConfig(); config = mapper.readValue(file, CharacterConfig.class); break;
                case Card.EFFECT: config = new EffectConfig(); config = mapper.readValue(file, EffectConfig.class); break;
                case Card.WEAPON: config = new WeaponConfig(); config = mapper.readValue(file, WeaponConfig.class); break;
                case Card.ARMOR: config = new ArmorConfig(); config = mapper.readValue(file, ArmorConfig.class); break;
                case Card.CONSUMABLE: config = new ConsumableConfig();config = mapper.readValue(file, ConsumableConfig.class); break;
                case Card.RUNE: config = new RuneConfig(); config = mapper.readValue(file, RuneConfig.class); break;
                case Card.ACCESSOIRE: config = new AccessoireConfig(); config = mapper.readValue(file, AccessoireConfig.class); break;
            }

            // Load images from paths
            cardFrame = getImageFromFile(config.cardFrame); cardFramePath = config.cardFrame;
            cardBackground = getImageFromFile(config.cardBackground); cardBackgroundPath = config.cardBackground;
            cardType = null; 
            if (config.hasRuneCut) getImageFromFile(RUNECUT);

            //EventBus.publish(new LoadConfigEvent(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    
}
