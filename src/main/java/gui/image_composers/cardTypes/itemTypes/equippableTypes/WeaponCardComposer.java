package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import events.CardLoadEvent;
import events.DiceUpdateEvent;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.EquippableCardComposer;

import java.awt.*;

public class WeaponCardComposer extends EquippableCardComposer{
    protected BufferedImage diceImage, attributeBaseImage, rangeTypeImage;
    protected AttributeLabel attributeLabel;

    protected int dice, attribute, rangeType;
    //protected OneLineTextPane rangeTextPane;
    private int[] diceFieldBounds = {530,440,180,180};
    private int[] tierFieldBounds = {550,440,180,180};
    private int[] attributeLabelBounds = {0,470,305,105};
    private int[] attributeBaseFieldBounds = {0,470,305,105};

    public WeaponCardComposer(int type){
        super(type);
        this.dice = 4;
        setRangeType(type);
        EventBus.subscribe(DiceUpdateEvent.class, this::onDiceUpdate);
        attributeLabel = new AttributeLabel(GlobalVar.STRENGTH, type, attributeLabelBounds[0],attributeLabelBounds[1],attributeLabelBounds[2],attributeLabelBounds[3],1.0);

        //EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
        //EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
    }

    public int getDice(){
        return dice;
    }

    public int getAttribute(){
        return attribute;
    }

    

    @Override
    public BufferedImage composeCard(double scale, int type){
        switch(type){
            case GlobalVar.REPAINT_ATTRIBUTE_LABEL:
                return paintAttributeLabel(scale);
            case GlobalVar.REPAINT_TIER_LABEL:
                return paintTierLabel(scale);
            case GlobalVar.REPAINT_ALL:
                return paintAll(scale);      
            default: 
                return super.composeCard(scale, type);
        }
    }

    private BufferedImage paintAll(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage finalImage = super.composeCard(scale,GlobalVar.REPAINT_ALL);
        Graphics2D g2d = finalImage.createGraphics();
        g2d.drawImage(paintAttributeLabel(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.drawImage(paintTierLabel(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return finalImage;
    }

    private BufferedImage paintAttributeLabel(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g2d = i.createGraphics();
        BufferedImage attributeImage = attributeLabel.paint(scale);
        if (attributeImage != null) {
            g2d.drawImage(attributeImage, (int)(attributeBaseFieldBounds[0]*scale), (int)(attributeBaseFieldBounds[1]*scale), (int)(attributeBaseFieldBounds[2]*scale), (int)(attributeBaseFieldBounds[3]*scale), null);
        }
        g2d.dispose();
        return i;
    }

    private BufferedImage paintTierLabel(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g2d = i.createGraphics();
        if (diceImage != null) {
            g2d.drawImage(diceImage, (int)(diceFieldBounds[0]*scale), (int)(diceFieldBounds[1]*scale), (int)(diceFieldBounds[2]*scale), (int)(diceFieldBounds[3]*scale), null);
        }
        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(tierFieldBounds[0]*scale), (int)(tierFieldBounds[1]*scale), (int)(tierFieldBounds[2]*scale), (int)(tierFieldBounds[3]*scale), null);;
        }
        g2d.dispose();
        return i;
    }


    @Override
    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.DICE: 
                diceImage = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
            default: 
                super.setField(field, path);
        }

        
    }

    protected void setRangeType(int type){

        this.rangeType = type;

        switch(type){
            case GlobalVar.W_MELEE: setField(GlobalVar.RANGE_TYPE, GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"melee.png"); break;
            case GlobalVar.W_THROWABLE: setField(GlobalVar.RANGE_TYPE, GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"ranged.png"); break;
            case GlobalVar.W_RANGED: setField(GlobalVar.RANGE_TYPE, GlobalVar.ATTRIBUTE_LABEL_COMPONENTS+"ranged.png"); break;
            
        }

    }

    protected void onDiceUpdate(DiceUpdateEvent e){
        this.dice = e.dice;
        setField(GlobalVar.DICE, GlobalVar.DICE_IMAGE_PATH+dice+".png");
    }

    protected void loadCard(CardLoadEvent e) {
        super.loadCard(e);
    }

    protected WeaponConfig writeToConfig(WeaponConfig config){
        super.writeToConfig(config);
        config.dice = runeCharges;
        config.attribute = attribute;
        //config.rangeText = rangeTextPane.getText();

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
    }

    
}
