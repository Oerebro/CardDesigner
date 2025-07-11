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
        //EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_RUNECHARGE_LABEL));
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
    public BufferedImage composeCard(double scale, int type){
        switch(type){
            case GlobalVar.REPAINT_RUNECHARGE_LABEL:
                return paintRuneCharge(scale);
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
        g2d.drawImage(paintRuneCharge(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return finalImage;
    }

    private BufferedImage paintRuneCharge(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g2d = i.createGraphics();
        if (runeChargesGlyph != null) {
            g2d.drawImage(runeChargesGlyph, (int) (0*scale), (int)(0*scale), (int) (750*scale), (int) (1050*scale), null);
        }
        g2d.dispose();
        return i;
    }


    @Override
    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.TIER: 
                tierGlyph = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
            case GlobalVar.RUNECHARGES: 
                runeChargesGlyph = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_RUNECHARGE_LABEL));
                break;
            case GlobalVar.RUNECUT: 
                runeCut = getImageFromFile(RUNECUT); 
                    if(path == null) {
                        hasRuneCut = false;
                    }else{
                        hasRuneCut=true;
                    }; 
                    EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_RUNECUT));
                    break;
            default: super.setField(field, path);
        }
    }

    protected void loadCard(CardLoadEvent e) {
        super.loadCard(e);
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
