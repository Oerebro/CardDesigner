package gui.image_composers.cardTypes.itemTypes;

import java.awt.image.BufferedImage;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.image_composers.cardTypes.ItemCardComposer;

import java.awt.*;

public class AccessoireCardComposer extends ItemCardComposer{

    private BufferedImage tierGlyph;
    private int tier;

    public int getTier(){
        return tier;
    }

    public AccessoireCardComposer(){
        super(GlobalVar.ACCESSOIRE);
    }

    @Override
    public BufferedImage composeCard(double scale, int type){
        BufferedImage finalImage = super.composeCard(scale,type);

        Graphics2D g2d = finalImage.createGraphics();

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(465*scale),  (int)(180*scale),  (int)(180*scale), null);
        }
        g2d.dispose();
        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        super.setField(field, path);

        switch(field){
            case GlobalVar.TIER: 
                tierGlyph = getImageFromFile(path); 
                tier = getTierFromPath(path); 
                EventBus.publish(new RepaintPanelEvent(GlobalVar.REPAINT_TIER_LABEL));
                break;
        }

        
    }

    private int getTierFromPath(String path){
        return Integer.parseInt(path.replaceAll("resources/glpyhs/tier", "").replaceAll(".png", ""));
    }

    
}
