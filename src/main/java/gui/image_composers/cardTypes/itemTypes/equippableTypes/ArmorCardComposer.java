package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.image.BufferedImage;

import events.EventBus;
import events.ImageUpdate;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.image_composers.cardTypes.itemTypes.EquippableCardComposer;

import java.awt.*;

public class ArmorCardComposer extends EquippableCardComposer{

    private BufferedImage ac1, ac2;

    private int ac = 0;

    public int getAC(){
        return ac;
    }

    public ArmorCardComposer(){
        super(GlobalVar.ARMOR);
    }

    @Override
    public BufferedImage composeCard(double scale, int type){
        switch(type){
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
        BufferedImage finalImage = super.composeCard(scale,type);
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
        if (ac1 != null) {
            g2d.drawImage(ac1, 0, 0, targetWidth, targetHeight, null);
        }

        if (ac2 != null) {
            g2d.drawImage(ac2, 0, 0, targetWidth, targetHeight, null);
        }

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, 0, 0, targetWidth, targetHeight, null);
        }
        g2d.dispose();
        return i;
    }


    protected void setField(int field, String path){
        super.setField(field, path);

        switch(field){
            case GlobalVar.ARMOR1: 
                ac1 = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
            case GlobalVar.ARMOR2: 
                ac2 = getImageFromFile(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
            default: super.setField(field, path);
        }
    }

    protected void onImageUpdate(ImageUpdate e){
        setField(e.type, e.path);
    }


    
}
