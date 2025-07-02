package gui.image_composers.cardTypes.itemTypes.equippableTypes;

import java.awt.image.BufferedImage;

import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.card_types.*;
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
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();


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
        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        super.setField(field, path);

        switch(field){
            case GlobalVar.ARMOR1: ac1 = getImageFromFile(path); EventBus.publish(new RepaintPanelEvent());break;
            case GlobalVar.ARMOR2: ac2 = getImageFromFile(path); EventBus.publish(new RepaintPanelEvent());break;
            default: super.setField(field, path);
        }
    }


    
}
