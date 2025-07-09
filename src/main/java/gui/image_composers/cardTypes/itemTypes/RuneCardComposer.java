package gui.image_composers.cardTypes.itemTypes;

import java.awt.image.BufferedImage;

import events.CardLoadEvent;
import events.EventBus;
import events.ItemImageUpdateEvent;
import events.RepaintPanelEvent;
import events.TierUpdate;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.ItemCardComposer;

import java.awt.*;

public class RuneCardComposer extends ItemCardComposer{

    private BufferedImage tierGlyph;
    private int tier;

    public int getTier(){
        return tier;
    }

    public RuneCardComposer(){
        super(GlobalVar.RUNE);
        System.out.println("Rune Composer");
        EventBus.subscribe(TierUpdate.class, this::onTierUpdate);
    }

    @Override
    protected void onLoadCard(CardLoadEvent e){
        super.onLoadCard(e);
    }

    @Override
    protected void onImageUpdate(ItemImageUpdateEvent e){
        super.setField(e.type, e.path);
    }
 
    public ItemConfig saveConfig(){
        return super.writeToConfig(new ItemConfig());
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
    }

    @Override
    public BufferedImage composeCard(double scale, int type){
        BufferedImage finalImage = super.composeCard(scale,type);

        Graphics2D g2d = finalImage.createGraphics();

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(512*scale),  (int)(180*scale),  (int)(180*scale), null);
        }
        g2d.dispose();
        return finalImage;
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
    protected void setField(int field, String path){
        super.setField(field, path);
        switch(field){
            case GlobalVar.TIER: 
            tierGlyph = getImageFromFile(path); 
            EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));break;
        }
    }

    
}
