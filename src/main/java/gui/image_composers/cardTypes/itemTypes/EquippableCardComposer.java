package gui.image_composers.cardTypes.itemTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import events.CardLoadEvent;
import events.EventBus;
import events.RepaintPanelEvent;
import events.RuneChargesUpdate;
import events.TextLoadEvent;
import events.TierUpdate;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.ItemCardComposer;

import java.awt.*;

public class EquippableCardComposer extends ItemCardComposer{

    protected BufferedImage tierGlyph, runeChargesGlyph;
    protected int tier, runeCharges;
    protected boolean hasRuneCut;

    public int getTier(){
        return tier;
    }

    public int runeCharges(){
        return runeCharges;
    }

    public EquippableCardComposer(int type){
        super(type);
        runeCharges = 0;
        EventBus.subscribe(RuneChargesUpdate.class, this::onRuneChargeUpdate);
        EventBus.subscribe(TierUpdate.class, this::onTierUpdate);
    }

    private void onRuneChargeUpdate(RuneChargesUpdate e){
        runeCharges = e.num;

        if(runeCharges > 0){
            setField(GlobalVar.RUNECHARGES, GlobalVar.RUNECHARGE_IMAGE_PATH+runeCharges+".png");
        }else{
            setField(GlobalVar.RUNECHARGES, null);
        }
    }

    private void onTierUpdate(TierUpdate e){
        tier = e.num;

        if(tier > 0){
            setField(GlobalVar.TIER, GlobalVar.TIER_IMAGE_PATH+tier+".png");
        }else{
            setField(GlobalVar.TIER, null);
        }
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        Graphics2D g2d = finalImage.createGraphics();

        /*if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(450*scale),  (int)(180*scale),  (int)(180*scale), null);
        }*/

        if (runeChargesGlyph != null) {
            g2d.drawImage(runeChargesGlyph, (int) (0*scale), (int)(0*scale), (int) (750*scale), (int) (1050*scale), null);
        }
        g2d.dispose();
        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        

        switch(field){
            case GlobalVar.TIER: tierGlyph = getImageFromFile(path); EventBus.publish(new RepaintPanelEvent());break;
            case GlobalVar.RUNECHARGES: runeChargesGlyph = getImageFromFile(path); EventBus.publish(new RepaintPanelEvent());break;
            case GlobalVar.RUNECUT: runeCut = getImageFromFile(RUNECUT); if(path == null) {hasRuneCut = false;}else{hasRuneCut=true;}; EventBus.publish(new RepaintPanelEvent());break;
            default: super.setField(field, path);
        }

        EventBus.publish(new RepaintPanelEvent());
    }

    @Override
    protected void onLoadCard(CardLoadEvent e) {
        super.onLoadCard(e);
    }

    protected EquippableConfig writeToConfig(EquippableConfig config){
        super.writeToConfig(config);
        config.runeCharges = runeCharges;
        config.tier = tier;

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
