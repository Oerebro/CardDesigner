package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

import events.EventBus;
import events.GetCardAttributesEvent;
import events.ImageUpdateEvent;
import events.InfoFontUpdate;
import events.InfoTextUpdate;
import events.LoadConfigEvent;
import events.RepaintPanelEvent;
import events.ToggleTitleBorder;
import events.CardLoadEvent;
import events.ClearUnrelatedImagesEvent;
import events.InfoTextUpdate;

import java.awt.*;
import gui.previewpanel.JScalingTextPane;
import gui.previewpanel.OneLineTextPane;

public class ImageComposer {
    private BufferedImage cardFrame, cardBackground, cardTextbox, cardTitle,cardItemImage, attributeImage,cardType,handedImage, tierGlyph, weaponType, runeSlot,runeCut,armorclass1,armorclass2,effectImage, overlayImage;
    private String cardFramePath, cardBackgroundPath, cardTextboxPath, cardTitlePath,cardItemImagePath, attributeImagePath,cardTypePath,handedImagePath, tierGlyphPath, weaponTypePath, runeSlotPath,overlayPath,runeCutPath,armorclass1Path,armorclass2Path,effectImagePath;
    private JScalingTextPane infoTextPane;
    private OneLineTextPane titleTextPane;
    private Boolean titleBorder;

    public ImageComposer(){
        EventBus.subscribe(ImageUpdateEvent.class, this::onImageUpdate);
        EventBus.subscribe(CardLoadEvent.class, this::onLoadCard);
        EventBus.subscribe(InfoTextUpdate.class, this::onInfoTextUpdate);
        EventBus.subscribe(ClearUnrelatedImagesEvent.class, this::onClearUnrelatedImages);
        EventBus.subscribe(ToggleTitleBorder.class, this::toggleTitleBorder);
        titleBorder = false;

        

        infoTextPane = new JScalingTextPane(9, 72);
        infoTextPane.setBounds2(65,655,620,340);
        infoTextPane.setSize(620,340);

        titleTextPane = new OneLineTextPane("title", 200, 80,25,590,80);
        titleTextPane.setOpaque(false);
        titleTextPane.setForeground(Color.WHITE);

        //titleTextPane.setBounds(80,20,590,80);
        //titleTextPane.setBounds(0,100,590,80);
        
        


    }

    private void toggleTitleBorder(ToggleTitleBorder e){
        titleBorder = e.bool;
        EventBus.publish(new RepaintPanelEvent());
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

        BufferedImage infoText = new BufferedImage((int)(620*scale),  (int)(340*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = infoText.createGraphics();
        infoTextPane.setSize((int)(620 * scale), (int)(340 * scale));
        infoTextPane.doLayout();
        infoTextPane.validate();
        infoTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        g2d.drawImage(infoText, (int) (65*scale), (int)(655*scale),  (int)(620*scale),  (int)(340*scale), null);

        BufferedImage titleText = new BufferedImage((int)(590*scale),  (int)(80*scale), BufferedImage.TYPE_INT_ARGB);
        System.out.println("Label text: " + titleTextPane.getText());
        labelGraphics = titleText.createGraphics();
        titleTextPane.setSize((int)(590 * scale), (int)(80 * scale));
        titleTextPane.revalidate();
        titleTextPane.repaint(); 
        titleTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(titleBorder){
            titleText = drawStroke(titleText,3,Color.WHITE);
        }
        g2d.drawImage(titleText, (int) (titleTextPane.getX()*scale), (int)(titleTextPane.getY()*scale),  (int)(590*scale),  (int)(80*scale), null);


        /*BufferedImage titleText = new BufferedImage(590, 80, BufferedImage.TYPE_INT_ARGB);

        Graphics2D labelGraphics2 = titleText.createGraphics();
        titleTextPane.printAll(labelGraphics2);
        labelGraphics2.dispose();
        /*if(controlPanel.getTitleStroke()){
            titleText = drawStroke(titleText,3,Color.WHITE);
        }
        g2d.drawImage(titleText, (int) (80*scale), (int) (20*scale),  (int) (590*scale),  (int) (80*scale), null);*/

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
        EventBus.publish(new InfoTextUpdate(""));
            cardFrame = getImageFromFile(e.frameImage); cardFramePath = e.frameImage;
            cardBackground = getImageFromFile(e.backgroundImage) ;cardBackgroundPath = e.backgroundImage;
            cardTextbox = getImageFromFile(e.textboxImage) ;cardTextboxPath = e.textboxImage;
            cardTitle = getImageFromFile(e.titleImage) ;cardTitlePath = e.titleImage;
    }

    private void onInfoTextUpdate(InfoTextUpdate e){
        EventBus.publish(new RepaintPanelEvent());
    }


    private void setField(String field, String path){
        switch(field){
            case "cardFrame": cardFrame = getImageFromFile(path); cardFramePath = path; break;
            case "cardBackground": cardBackground = getImageFromFile(path); cardBackgroundPath = path; break;
            case "cardTextbox": cardTextbox = getImageFromFile(path); cardTextboxPath = path; break;
            case "cardTitle": cardTitle = getImageFromFile(path); cardTitlePath = path; break;
            case "cardItemImage":   cardItemImage = getImageFromFile(path); cardItemImagePath = path; 
                                    overlayImage = getImageFromFile(getOverlayImagePath(path)); overlayPath = getOverlayImagePath(path); break;
            case "attributeImage": attributeImage = getImageFromFile(path); attributeImagePath = path; break;
            case "cardType": cardType = getImageFromFile(path); cardTypePath = path; break;
            case "handedImage": handedImage = getImageFromFile(path); handedImagePath = path; break;
            case "tierGlyph": tierGlyph = getImageFromFile(path); tierGlyphPath = path; break;
            case "runeSlot": runeSlot = getImageFromFile(path); runeSlotPath = path; break;
            case "weaponType": weaponType = getImageFromFile(path); weaponTypePath = path; break;
            case "runeCutTemplate": runeCut = getImageFromFile(path); runeCutPath = path; break;
            case "ac1": armorclass1 = getImageFromFile(path); armorclass1Path = path; break;
            case "ac2": armorclass2 = getImageFromFile(path); armorclass2Path = path; break;
            case "effect": effectImage = getImageFromFile(path); effectImagePath = path; break;
        }

        EventBus.publish(new RepaintPanelEvent());
    }

    private String getOverlayImagePath(String path){
        String[] arr = path.split("\\\\");
        System.out.println(arr[arr.length-1]);
        return "resources/img/overlay/"+arr[arr.length-1];
    }


    private BufferedImage getImageFromFile(String path){
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
        ImageComposerConfig config = new ImageComposerConfig();

        config.cardFrame = cardFramePath;
        config.cardBackground = cardBackgroundPath;
        config.cardTextbox = cardTextboxPath;
        config.cardTitle = cardTitlePath;
        config.cardItemImage = cardItemImagePath;
        config.attributeImage = attributeImagePath;
        config.cardType = cardTypePath;
        config.handedImage = handedImagePath;
        config.tierGlyph = tierGlyphPath;
        config.weaponType = weaponTypePath;
        //config.runeSlot = runeSlotPath;
        config.runeCut = runeCutPath;
        config.armorclass1 = armorclass1Path;
        config.armorclass2 = armorclass2Path;
        config.effectImage = effectImagePath;

        config.infoFont = infoTextPane.getFontName();
        config.infoText = infoTextPane.getText();

        config.titleFont = titleTextPane.getFont().getName();
        config.titleText = titleTextPane.getText();
        config.hasTitleBorder = titleBorder;


        EventBus.publish(new GetCardAttributesEvent(config));

        try {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ImageComposerConfig config = mapper.readValue(file, ImageComposerConfig.class);

            // Load images from paths
            cardFrame = getImageFromFile(config.cardFrame); cardFramePath = config.cardFrame;
            cardBackground = getImageFromFile(config.cardBackground); cardBackgroundPath = config.cardBackground;
            cardTextbox = getImageFromFile(config.cardTextbox); cardTextboxPath = config.cardTextbox;
            cardTitle = getImageFromFile(config.cardTitle); cardTitlePath = config.cardTitle;
            cardItemImage = getImageFromFile(config.cardItemImage); cardItemImagePath = config.cardItemImage; //overlayImage = getImageFromFile(config.cardOverlay);
            attributeImage = getImageFromFile(config.attributeImage); attributeImagePath = config.attributeImage;
            cardType = getImageFromFile(config.cardType); cardTypePath = config.cardType;
            handedImage = getImageFromFile(config.handedImage); handedImagePath = config.handedImage;
            tierGlyph = getImageFromFile(config.tierGlyph); tierGlyphPath = config.tierGlyph;
            weaponType = getImageFromFile(config.weaponType); weaponTypePath = config.weaponType;
            //runeSlot = getImageFromFile(config.runeSlot); runeSlotPath = config.runeSlot;
            runeCut = getImageFromFile(config.runeCut); runeCutPath = config.runeCut;
            armorclass1 = getImageFromFile(config.armorclass1); armorclass1Path = config.armorclass1;
            armorclass2 = getImageFromFile(config.armorclass2); armorclass2Path = config.armorclass2;
            effectImage = getImageFromFile(config.effectImage); effectImagePath = config.effectImage;

            EventBus.publish(new LoadConfigEvent(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    
}
