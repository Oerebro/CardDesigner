package gui.image_composers.cardTypes.itemTypes.equippableTypes.weaponTypes;

import java.awt.image.BufferedImage;

import events.CardLoadEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.itemTypes.equippableTypes.WeaponCardComposer;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class WeaponRangedCardComposer extends WeaponCardComposer{

    public WeaponRangedCardComposer(){
        super(GlobalVar.W_RANGED);
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();

        BufferedImage rangeText = new BufferedImage((int)(670*scale),  (int)(60*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = rangeText.createGraphics();
        /*rangeTextPane.setSize((int)(670 * scale), (int)(60 * scale));
        rangeTextPane.doLayout();
        rangeTextPane.validate();
        rangeTextPane.printAll(labelGraphics);*/
        labelGraphics.dispose();
        g2d.drawImage(rangeText, (int) (40*scale), (int)(590*scale),  (int)(670*scale),  (int)(60*scale), null);
        g2d.dispose();
        return finalImage;

    }

    @Override
    protected void onLoadCard(CardLoadEvent e){
        super.onLoadCard(e);
    }

    public WeaponConfig writeToConfig(){
        return super.writeToConfig(new WeaponConfig());
    }

    public void loadFromConfig(CardConfig config) {
        super.loadFromConfig(config);
    }
    
}
