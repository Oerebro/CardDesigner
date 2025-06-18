package gui.image_composers;

import java.awt.image.BufferedImage;

import gui.card_types.*;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class WeaponThrowableCardComposer extends EquippableCardComposer{

    protected OneLineTextPane rangeTextPane;

    public WeaponThrowableCardComposer(){
        super(Card.W_THROWABLE);
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();

        BufferedImage rangeText = new BufferedImage((int)(670*scale),  (int)(60*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = rangeText.createGraphics();
        rangeTextPane.setSize((int)(670 * scale), (int)(60 * scale));
        rangeTextPane.doLayout();
        rangeTextPane.validate();
        rangeTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        g2d.drawImage(rangeText, (int) (40*scale), (int)(590*scale),  (int)(670*scale),  (int)(60*scale), null);

        return finalImage;

    }
    
}
