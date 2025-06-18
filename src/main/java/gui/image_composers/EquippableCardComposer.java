package gui.image_composers;

import java.awt.image.BufferedImage;

import events.CardLoadEvent;
import events.EventBus;
import events.RepaintPanelEvent;
import events.RuneChargesUpdate;
import events.TierUpdate;
import gui.card_types.*;

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

        EventBus.subscribe(RuneChargesUpdate.class, this::onRuneChargeUpdate);
        EventBus.subscribe(TierUpdate.class, this::onTierUpdate);
    }

    private void onRuneChargeUpdate(RuneChargesUpdate e){
        runeCharges = e.num;

        if(runeCharges > 0){
            setField(Card.RUNECHARGES, "resources/glyphs/runecharge/"+runeCharges+".png");
        }else{
            setField(Card.RUNECHARGES, null);
        }
    }

    private void onTierUpdate(TierUpdate e){
        tier = e.num;

        if(tier > 0){
            setField(Card.TIER, "resources/glyphs/runecharge/"+tier+".png");
        }else{
            setField(Card.TIER, null);
        }
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        Graphics2D g2d = finalImage.createGraphics();

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(465*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        if (runeChargesGlyph != null) {
            g2d.drawImage(runeChargesGlyph, (int) (0*scale), (int)(0*scale), (int) (750*scale), (int) (1050*scale), null);
        }

        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        

        switch(field){
            case Card.TIER: tierGlyph = getImageFromFile(path); break;
            case Card.RUNECHARGES: runeChargesGlyph = getImageFromFile(path); break;
            case Card.RUNECUT: runeCut = getImageFromFile(path); if(path == null) {hasRuneCut = false;}else{hasRuneCut=true;}; break;
            default: super.setField(field, path);
        }

        EventBus.publish(new RepaintPanelEvent());
    }

    @Override
    protected void onLoadCard(CardLoadEvent e) {
        super.onLoadCard(e);
    }

    
}
