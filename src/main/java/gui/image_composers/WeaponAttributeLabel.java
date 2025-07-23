package gui.image_composers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import events.AttributeUpdate;
import events.CardTypeUpdate;
import events.EventBus;
import events.RepaintPanelEvent;
import events.TextUpdate;
import gui.GlobalVar;
import gui.Loggable;
import gui.previewpanel.OneLineTextPane;

public class WeaponAttributeLabel extends Loggable {

    

    private BufferedImage baseLabel, attributeIcon, damageTypeIcon, meleeLabel, rangedLabel;
    private OneLineTextPane rangeNormal, rangeMax;
    private int attribute, damageType;

    private int[] rangeNormalBounds = {169,47,42+10,55};
    private int[] rangeMaxBounds = {215,47,77+10,55};
    private int[] bounds = {0,0,305,105};
    private int[] damageTypeBounds = {123,54,44,44};
    private int[] attributeIconBounds = {27,13,85,85};

    public WeaponAttributeLabel(int attribute, int damageType, int x, int y, int width, int height, double scale){
        this.damageType = damageType;
        EventBus.subscribe(AttributeUpdate.class, this::setAttribute);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        //this.setOpaque(false);
        //this.setBounds(x,y,width,height);
        this.attribute = GlobalVar.STRENGTH;
        attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"strength.png");
        setDamageType(damageType);
        setBaseLabel(damageType);

        rangeNormal = new OneLineTextPane(GlobalVar.RANGE_NORMAL_TEXT_UPDATE, 50, (int)(rangeNormalBounds[0]*scale), (int)(rangeNormalBounds[1]*scale), (int)(rangeNormalBounds[2]*scale), (int)(rangeNormalBounds[3]*scale));
        rangeMax = new OneLineTextPane(GlobalVar.RANGE_MAX_TEXT_UPDATE, 50, (int)(rangeMaxBounds[0]*scale), (int)(rangeMaxBounds[1]*scale), (int)(rangeMaxBounds[2]*scale), (int)(rangeMaxBounds[3]*scale));

        rangeNormal.setText("");
        rangeMax.setText("");
        rangeNormal.setForeground(Color.BLACK);
        rangeMax.setForeground(Color.BLACK);

        EventBus.subscribe(CardTypeUpdate.class, this::onCardTypeUpdate);
    }

    private void onCardTypeUpdate(CardTypeUpdate e){
            setDamageType(e.type);
    }

    private void onTextUpdate(TextUpdate e){
        if(e.type != GlobalVar.RANGE_MAX_TEXT_UPDATE)
            return;
        if((e.text.equals(""))){
            setBaseLabel(GlobalVar.W_MELEE);
        }else{
            setBaseLabel(GlobalVar.W_RANGED);
        }
    }

    private void setAttribute(AttributeUpdate e){
        this.attribute = e.type;

        switch(attribute){
            case GlobalVar.STRENGTH:
                attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"strength.png");
                break;
            case GlobalVar.DEXTERITY:
                attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"dexterity.png");
                break;
            case GlobalVar.CONSTITUTION:
                attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"constitution.png");
                break;
            case GlobalVar.INTELLIGENCE:
                attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"intelligence.png");
                break;
            case GlobalVar.WISDOM:
                attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"wisdom.png");
                break;
            case GlobalVar.CHARISMA:
                attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"charisma.png");
                break;
        }
        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
    }

    private void setBaseLabel(int type){
        this.damageType = type;

        switch (type) {
            case GlobalVar.W_MELEE:       
                if(meleeLabel == null){
                    String path = GlobalVar.ATTRIBUTE_LABEL_COMPONENTS;
                    path += "melee.png";
                    meleeLabel = loadImageFromFile(path);
                }
                baseLabel = meleeLabel;
                break;
        
            default:
                if(rangedLabel == null){
                        String path = GlobalVar.ATTRIBUTE_LABEL_COMPONENTS;
                        path += "ranged.png";
                        rangedLabel = loadImageFromFile(path);
                    }
                baseLabel = rangedLabel;
                break;
        }

    }

    private void setDamageType(int type){
        this.damageType = type;

        switch(type){
            case GlobalVar.W_MAGIC:
                damageTypeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"damage_magic.png");
                break;
            case GlobalVar.W_MELEE:
                damageTypeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"damage_melee.png");
                break;
            case GlobalVar.W_RANGED:
                damageTypeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"damage_ranged.png");
        }

        EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
    }


    public BufferedImage paint(double scale){
        BufferedImage image = new BufferedImage((int) (bounds[2]*scale), (int) (bounds[3]*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(baseLabel, (int) (bounds[0] * scale), (int) (bounds[1] * scale),  (int) (bounds[2] * scale),(int) (bounds[3] * scale), null);

        if(damageTypeIcon != null){
            g2d.drawImage(damageTypeIcon, (int) (damageTypeBounds[0] * scale), (int) (damageTypeBounds[1] * scale),  (int) (damageTypeBounds[2] * scale),(int) (damageTypeBounds[3] * scale), null);
        }

        if(attributeIcon != null){
            g2d.drawImage(attributeIcon, (int) (attributeIconBounds[0] * scale), (int) (attributeIconBounds[1] * scale),  (int) (attributeIconBounds[2] * scale),(int) (attributeIconBounds[3] * scale), null);
        }

        BufferedImage text = new BufferedImage((int) (rangeNormalBounds[2] * scale),(int) (rangeNormalBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D text2d = text.createGraphics();
        rangeNormal.setSize((int)(rangeNormalBounds[2] * scale), (int)(rangeNormalBounds[3] * scale));
        rangeNormal.doLayout();
        rangeNormal.printAll(text2d);
        g2d.drawImage(drawStroke(text, 3, Color.WHITE), (int) (rangeNormalBounds[0] * scale), (int) (rangeNormalBounds[1] * scale),  (int) (rangeNormalBounds[2] * scale),(int) (rangeNormalBounds[3] * scale), null);
        
        text = new BufferedImage((int) (rangeMaxBounds[2] * scale),(int) (rangeMaxBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        text2d = text.createGraphics();
        rangeMax.setSize((int)(rangeMaxBounds[2] * scale), (int)(rangeMaxBounds[3] * scale));
        rangeMax.doLayout();      
        rangeMax.printAll(text2d);
        g2d.drawImage(drawStroke(text, 3, Color.WHITE), (int) (rangeMaxBounds[0] * scale), (int) (rangeMaxBounds[1] * scale),  (int) (rangeMaxBounds[2] * scale),(int) (rangeMaxBounds[3] * scale), null);

        text2d.dispose();
        g2d.dispose();

        return image;
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

    private BufferedImage loadImageFromFile(String path){
        BufferedImage i = null;
        try{
            i = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("Error on WeaponAttributeLabel::getImageFromFile ("+path+"); File not found");
        }
        return i;
    }
    
}
