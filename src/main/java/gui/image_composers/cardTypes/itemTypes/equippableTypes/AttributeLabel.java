package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import events.AttributeUpdate;
import events.EventBus;
import events.RepaintPanelEvent;
import events.TitleFontUpdate;
import gui.GlobalVar;
import gui.Loggable;
import gui.previewpanel.OneLineTextPane;

public class AttributeLabel extends Loggable {
    private BufferedImage baseLabel, attributeIcon, damageTypeIcon;
    private OneLineTextPane rangeNormal, rangeMax;
    private int attribute, damageType;

    private int[] rangeNormalBounds = {169,47,42+10,55};
    private int[] rangeMaxBounds = {213,47,77+10,55};
    private int[] bounds = {0,0,305,105};
    private int[] damageTypeBounds = {123,54,44,44};
    private int[] attributeIconBounds = {27,13,85,85};

    public AttributeLabel(int attribute, int damageType, int x, int y, int width, int height, double scale){
        EventBus.subscribe(AttributeUpdate.class, this::setAttribute);
        //this.setOpaque(true);
        //this.setBounds(x,y,width,height);
        this.attribute = GlobalVar.STRENGTH;
        attributeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_IMAGE_PATH+"strength.png");
        setDamageType(damageType);
        setBaseLabel(GlobalVar.W_MELEE);

        rangeNormal = new OneLineTextPane(OneLineTextPane.RANGE_NORMAL, 50, (int)(rangeNormalBounds[0]*scale), (int)(rangeNormalBounds[1]*scale), (int)(rangeNormalBounds[2]*scale), (int)(rangeNormalBounds[3]*scale));
        rangeMax = new OneLineTextPane(OneLineTextPane.RANGE_MAX, 50, (int)(rangeMaxBounds[0]*scale), (int)(rangeMaxBounds[1]*scale), (int)(rangeMaxBounds[2]*scale), (int)(rangeMaxBounds[3]*scale));

        rangeNormal.setText("5");
        rangeMax.setText("");
        rangeNormal.setForeground(Color.BLACK);
        rangeMax.setForeground(Color.BLACK);
    }

    private void setAttribute(AttributeUpdate e){
        log("att");
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
        EventBus.publish(new RepaintPanelEvent());
    }

    private void setDamageType(int type){
        this.damageType = type;

        switch(type){
            case GlobalVar.DAMAGE_MAGIC:
                damageTypeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"damage_magic.png");
                break;
            case GlobalVar.DAMAGE_MELEE:
                damageTypeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"damage_melee.png");
                break;
            case GlobalVar.DAMAGE_RANGED:
                damageTypeIcon = loadImageFromFile(GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"damage_ranged.png");
        }

        EventBus.publish(new RepaintPanelEvent());
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
        g2d.drawImage(text, (int) (rangeNormalBounds[0] * scale), (int) (rangeNormalBounds[1] * scale),  (int) (rangeNormalBounds[2] * scale),(int) (rangeNormalBounds[3] * scale), null);

        text = new BufferedImage((int) (rangeMaxBounds[2] * scale),(int) (rangeMaxBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        text2d = text.createGraphics();
        rangeMax.setSize((int)(rangeMaxBounds[2] * scale), (int)(rangeMaxBounds[3] * scale));
        rangeMax.doLayout();      
        rangeMax.printAll(text2d);
        g2d.drawImage(text, (int) (rangeMaxBounds[0] * scale), (int) (rangeMaxBounds[1] * scale),  (int) (rangeMaxBounds[2] * scale),(int) (rangeMaxBounds[3] * scale), null);

        text2d.dispose();
        g2d.dispose();

        return image;
    }

    private void setBaseLabel(int type){
        String path = GlobalVar.ATTRIBUTE_LABEL_COMPONENTS;

        switch (type) {
            case GlobalVar.W_MELEE:
                path += "melee.png";
                break;
        
            case GlobalVar.W_RANGED:
                path += "ranged.png";
                break;
        }
            baseLabel = loadImageFromFile(path);

    }

    private BufferedImage loadImageFromFile(String path){
        BufferedImage i = null;
        try{
            i = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("Error on AttributeLabel::getImageFromFile ("+path+"); File not found");
        }
        return i;
    }
    
}
