package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import org.apache.commons.math3.analysis.function.Log;

import events.CardLoadEvent;
import events.DiceUpdateEvent;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.EquippableCardComposer;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class WeaponCardComposer extends EquippableCardComposer{

    protected BufferedImage diceImage, attributeImage, rangeTypeImage;

    protected int dice, attribute, rangeType;
    protected OneLineTextPane rangeTextPane;
    private int[] diceFieldBounds = {530,450,180,180};
    private int[] tierFieldBounds = {550,450,180,180};
    private int[] rangeTypeBounds = {0,470,305,105};
    private int[] attributeFieldBounds = {0,470,305,105};

    public int getDice(){
        return dice;
    }

    public int getAttribute(){
        return attribute;
    }

    public WeaponCardComposer(int type){
        super(type);
        this.dice = 4;
        setRangeType(type);
        EventBus.subscribe(DiceUpdateEvent.class, this::onDiceUpdate);
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();


        if (rangeTypeImage != null) {
            g2d.drawImage(rangeTypeImage, (int)(rangeTypeBounds[0]*scale), (int)(rangeTypeBounds[1]*scale), (int)(rangeTypeBounds[2]*scale), (int)(rangeTypeBounds[3]*scale), null);
        }

        /*if (attributeImage != null) {
            g2d.drawImage(attributeImage, (int)(attributeFieldBounds[0]*scale), (int)(attributeFieldBounds[1]*scale), (int)(attributeFieldBounds[2]*scale), (int)(attributeFieldBounds[3]*scale), null);
        }*/

        if (diceImage != null) {
            g2d.drawImage(diceImage, (int)(diceFieldBounds[0]*scale), (int)(diceFieldBounds[1]*scale), (int)(diceFieldBounds[2]*scale), (int)(diceFieldBounds[3]*scale), null);
        }

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(tierFieldBounds[0]*scale), (int)(tierFieldBounds[1]*scale), (int)(tierFieldBounds[2]*scale), (int)(tierFieldBounds[3]*scale), null);;
        }
        g2d.dispose();
        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.DICE: diceImage = getImageFromFile(path); EventBus.publish(new RepaintPanelEvent());break;
            case GlobalVar.ATTRIBUTE: attributeImage = getImageFromFile(path); attribute = getAttribute(path); EventBus.publish(new RepaintPanelEvent()); break;
            case GlobalVar.RANGE_TYPE: rangeTypeImage = getImageFromFile(path); EventBus.publish(new RepaintPanelEvent()); break;
            default: super.setField(field, path);
        }

        
    }

    protected void setRangeType(int type){
        log(type);

        this.rangeType = type;

        switch(type){
            case GlobalVar.W_MELEE: setField(GlobalVar.RANGE_TYPE, GlobalVar.RANGETYPE_IMAGE_PATH+"melee.png"); break;
            case GlobalVar.W_THROWABLE: setField(GlobalVar.RANGE_TYPE, GlobalVar.RANGETYPE_IMAGE_PATH+"ranged.png"); break;
            case GlobalVar.W_RANGED: setField(GlobalVar.RANGE_TYPE, GlobalVar.RANGETYPE_IMAGE_PATH+"ranged.png"); break;
            
        }

    }

    protected void onDiceUpdate(DiceUpdateEvent e){
        this.dice = e.dice;
        setField(GlobalVar.DICE, GlobalVar.DICE_IMAGE_PATH+dice+".png");
    }

    private int getAttribute(String path){
        path = path.replaceAll(GlobalVar.ATTRIBUTE_IMAGE_PATH, "").replaceAll(".png", "");

        switch(path){
            case "strength": return GlobalVar.STRENGTH;
            case "constitution": return GlobalVar.CONSTITUTION;
            case "dexterity": return GlobalVar.DEXTERITY;
            case "wisdom": return GlobalVar.WISDOM;
            case "intelligence": return GlobalVar.INTELLIGENCE;
            case "charisma": return GlobalVar.CHARISMA;
        }

        return GlobalVar.STRENGTH;
    }

    @Override
    protected void onLoadCard(CardLoadEvent e) {
        super.onLoadCard(e);
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
