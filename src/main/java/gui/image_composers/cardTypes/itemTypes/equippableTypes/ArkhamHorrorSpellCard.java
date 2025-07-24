package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import javax.swing.SwingConstants;
import javax.swing.text.StyleConstants;

import events.CardLoadEvent;
import events.EventBus;
import events.ImageUpdate;
import events.RepaintPanelEvent;
import events.TextUpdate;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.EquippableCardComposer;
import gui.image_composers.components.OneLineTextPane;

import java.awt.*;

public class ArkhamHorrorSpellCard extends EquippableCardComposer{
    protected BufferedImage elderSign, rangeTypeBaseImage, rangeTypeImage, damageBaseImage, cardSide;

    protected int loreCount, attribute, rangeType;
    protected OneLineTextPane damage;

    protected int[] elderSignBounds = {650,970,60,60};
    protected int[] successesTextBounds = {585,970,60,60};
    protected int[] rangeTypeImageBounds = {0,445,160,160};
    protected int[] damageTextBounds = {620,465,76,90};
    protected int[] damageImageBounds = {565,420,190,190};
    protected int[] cardSideImageBounds = {10,15,60,60};

    private OneLineTextPane successesNeed;

    public ArkhamHorrorSpellCard(){
        super(GlobalVar.ARKHAM);

        titleImageBounds = setArray(0,435,750,200);
        textboxBounds = setArray(0,635,750,450);
        titleTextBounds = setArray(140,560,470,60);
        infoTextBounds = setArray(45,710,660,220);
        typeTextBounds = setArray(85,620,580,40);
        typeTextPane.setHorizontalAlignment(SwingConstants.CENTER);
        hasTitleBorder = false;
        hasTypeBorder = false;
        hasInfoBorder = false;
        dynamicTextboxPosition = false;
        infoTextPane.setStyleConstantAlignement(StyleConstants.ALIGN_CENTER);

        damage = new OneLineTextPane(GlobalVar.OTHER_TEXT_UPDATE_2, damageTextBounds);
        damage.setForeground(Color.BLACK);
        damage.setHorizontalAlignment(SwingConstants.CENTER);
        successesNeed = new OneLineTextPane(GlobalVar.OTHER_TEXT_UPDATE_1, successesTextBounds);
        successesNeed.setForeground(Color.RED);
        successesNeed.setHorizontalAlignment(SwingConstants.RIGHT);
        EventBus.subscribe(CardLoadEvent.class, this::loadCard);
        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);

        //elderSign = getImageFromFile(GlobalVar.GLYPH_PATH+"arkham/lore.png");
        //cardSide = getImageFromFile(GlobalVar.GLYPH_PATH+"arkham/frontside.png");
    }
    


    @Override
    public BufferedImage composeCard(double scale, int type){
        switch(type){
            case GlobalVar.REPAINT_ATTRIBUTE_LABEL:
                return paintAttributeLabel(scale);
            /*case GlobalVar.REPAINT_TIER_LABEL:
                return paintTierLabel(scale);*/
            case GlobalVar.REPAINT_ALL:
                return paintAll(scale);      
            default: 
                return super.composeCard(scale, type);
        }
    }

    protected void onImageUpdate(ImageUpdate e){
        setField(e.type, e.path);
    }

    protected void onTextUpdate(TextUpdate e){
        if(e.type == GlobalVar.OTHER_TEXT_UPDATE_1 || e.type == GlobalVar.OTHER_TEXT_UPDATE_2){ 
            switch(e.type){
                case GlobalVar.OTHER_TEXT_UPDATE_2:
                    damage.setText(e.text);
                    if(e.text.equals("")){
                        damageBaseImage = null;
                    }else{
                        damageBaseImage = getImageFromFile(GlobalVar.GLYPH_PATH+"arkham/blood.png");
                    }
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
                    break;
                case GlobalVar.OTHER_TEXT_UPDATE_1:
                    successesNeed.setText(e.text);
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
                    break;
            }
        }
    }

    private BufferedImage paintAll(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage finalImage = super.composeCard(scale,GlobalVar.REPAINT_ALL);
        Graphics2D g2d = finalImage.createGraphics();
        g2d.drawImage(paintAttributeLabel(scale), 0, 0, targetWidth, targetHeight, null);
       // g2d.drawImage(paintTierLabel(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return finalImage;
    }

    //elder sign
    private BufferedImage paintAttributeLabel(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g2d = i.createGraphics();
        g2d.drawImage(rangeTypeBaseImage, (int)(rangeTypeImageBounds[0]*scale), (int)(rangeTypeImageBounds[1]*scale), (int)(rangeTypeImageBounds[2]*scale), (int)(rangeTypeImageBounds[3]*scale), null);
        g2d.drawImage(rangeTypeImage, (int)(rangeTypeImageBounds[0]*scale), (int)(rangeTypeImageBounds[1]*scale), (int)(rangeTypeImageBounds[2]*scale), (int)(rangeTypeImageBounds[3]*scale), null);
        g2d.drawImage(elderSign, (int)(elderSignBounds[0]*scale), (int)(elderSignBounds[1]*scale), (int)(elderSignBounds[2]*scale), (int)(elderSignBounds[3]*scale), null);
        g2d.drawImage(damageBaseImage, (int)(damageImageBounds[0]*scale), (int)(damageImageBounds[1]*scale), (int)(damageImageBounds[2]*scale), (int)(damageImageBounds[3]*scale), null);
        
        BufferedImage img = new BufferedImage((int) (damageTextBounds[2] * scale), (int) (damageTextBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = img.createGraphics();
        damage.setSize((int) (damageTextBounds[2] * scale), (int) (damageTextBounds[3] * scale));   
        damage.revalidate();
        damage.repaint(); 
        damage.printAll(labelGraphics);
        g2d.drawImage(img, (int)(damageTextBounds[0]*scale), (int)(damageTextBounds[1]*scale), (int)(damageTextBounds[2]*scale), (int)(damageTextBounds[3]*scale), null);
        labelGraphics.dispose();

        img = new BufferedImage((int) (successesTextBounds[2] * scale), (int) (successesTextBounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
        labelGraphics = img.createGraphics();
        successesNeed.setSize((int) (successesTextBounds[2] * scale), (int) (successesTextBounds[3] * scale));   
        successesNeed.revalidate();
        successesNeed.repaint(); 
        successesNeed.printAll(labelGraphics);
        g2d.drawImage(img, (int)(successesTextBounds[0]*scale), (int)(successesTextBounds[1]*scale), (int)(successesTextBounds[2]*scale), (int)(successesTextBounds[3]*scale), null);
        labelGraphics.dispose();

        //g2d.drawImage(cardSide, (int)(cardSideImageBounds[0]*scale), (int)(cardSideImageBounds[1]*scale), (int)(cardSideImageBounds[2]*scale), (int)(cardSideImageBounds[3]*scale), null);

        g2d.dispose();
        return i;
    }



    @Override
    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.ARKHAM_RANGE_IMAGE_UPDATE:
                rangeTypeImage = getImageFromFile(path);
                rangeTypeBaseImage = getImageFromFile(GlobalVar.GLYPH_PATH+"arkham/rangetype_base.png");
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
                break;
            case GlobalVar.ARKHAM_RANGE_IMAGE_UPDATE_NONE:
                rangeTypeImage = null;
                rangeTypeBaseImage = null;
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
                break;
            case GlobalVar.ARKHAM_CARDSIDE_IMAGE_UPDATE:
                cardSide = getImageFromFile(path);
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_ATTRIBUTE_LABEL));
            default: 
                super.setField(field, path);
            
        }

        
    }


    protected void loadCard(CardLoadEvent e) {
        e.titleImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"title/arkham_spell.png";
        e.textBoxImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"textbox/arkham_spell.png";
        e.frameImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"frame/eldritch.png";
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