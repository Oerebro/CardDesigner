package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.image.BufferedImage;


import events.CardLoadEvent;
import events.EventBus;
import events.ImageUpdate;
import events.RepaintPanelEvent;
import events.TierUpdate;
import gui.GlobalVar;
import gui.image_composers.cardTypes.itemTypes.EquippableCardComposer;

import java.awt.*;

public class ArmorCardComposer extends EquippableCardComposer{

    private BufferedImage ac1, ac2;

    private int ac = 0;
    private int[] tierFieldBounds = {511,401,258,258};
    private int[] acBounds = {580,470,120,120};
    BufferedImage normal = getImageFromFile(GlobalVar.AC_IMAGE_PATH+"armor.png");
    BufferedImage uncommon = getImageFromFile(GlobalVar.AC_IMAGE_PATH+"uncommon_armor.png");
    BufferedImage rare = getImageFromFile(GlobalVar.AC_IMAGE_PATH+"rare_armor.png");
    BufferedImage legendary = getImageFromFile(GlobalVar.AC_IMAGE_PATH+"legendary_armor.png");
    BufferedImage artifact = getImageFromFile(GlobalVar.AC_IMAGE_PATH+"artifact_armor.png");

    public ArmorCardComposer(){
        super(GlobalVar.ARMOR);
        tierGlyph = getTierImage(0);
        EventBus.subscribe(CardLoadEvent.class, this::onLoadCard);
        EventBus.subscribe(TierUpdate.class, this::onTierUpdate);
        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
    }

    public int getAC(){
        return ac;
    }

    @Override
    protected void onTierUpdate(TierUpdate e){
        if(this.tier == e.num) return;

        tier = e.num;
        tierGlyph = getTierImage(tier);
    }

    private BufferedImage getTierImage(int tier){
        switch (tier) {
            case 1:
                setField(GlobalVar.TIER, GlobalVar.AC_IMAGE_PATH+"uncommon_armor.png");
                return uncommon;
            case 2:
                setField(GlobalVar.TIER, GlobalVar.AC_IMAGE_PATH+"rare_armor.png");
                return rare;
            case 3:
                setField(GlobalVar.TIER, GlobalVar.AC_IMAGE_PATH+"legendary_armor.png");
                return legendary;
            case 4:
                setField(GlobalVar.TIER, GlobalVar.AC_IMAGE_PATH+"artifact_armor.png");
                return artifact;
            default:
                setField(GlobalVar.TIER, GlobalVar.AC_IMAGE_PATH+"armor.png");
                return normal;
        }
    }
    

    protected void onLoadCard(CardLoadEvent e){
        super.loadCard(e);
    }

    @Override
    public BufferedImage composeCard(double scale, int type){
        switch(type){
            case GlobalVar.REPAINT_ATTRIBUTE_LABEL:
                return null;
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
        g2d.drawImage(paintTierLabel(scale), 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return finalImage;
    }

    private BufferedImage paintTierLabel(double scale){
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);
        BufferedImage i = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g2d = i.createGraphics();
        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(tierFieldBounds[0]*scale), (int)(tierFieldBounds[1]*scale), (int)(tierFieldBounds[2]*scale), (int)(tierFieldBounds[3]*scale), null);
        }
        if (ac1 != null) {
            g2d.drawImage(ac1, (int)(acBounds[0]*scale), (int)(acBounds[1]*scale), (int)(acBounds[2]*scale), (int)(acBounds[3]*scale), null);
        }

        if (ac2 != null) {
            g2d.drawImage(ac2,(int)(acBounds[0]*scale), (int)(acBounds[1]*scale), (int)(acBounds[2]*scale), (int)(acBounds[3]*scale), null);
        }
        g2d.dispose();
        return i;
    }

    @Override
    protected void setField(int field, String path){
        switch(field){
            case GlobalVar.ARMOR1: 
                ac1 = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
            case GlobalVar.ARMOR2: 
                ac2 = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
            default: 
                super.setField(field, path);
        }
    }

    protected void onImageUpdate(ImageUpdate e){
        setField(e.type, e.path);
    }


    
}
