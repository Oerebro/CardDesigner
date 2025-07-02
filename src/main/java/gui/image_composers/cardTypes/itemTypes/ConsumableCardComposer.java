package gui.image_composers.cardTypes.itemTypes;

import java.awt.image.BufferedImage;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.GlobalVar;
import gui.card_types.*;
import gui.image_composers.cardTypes.ItemCardComposer;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class ConsumableCardComposer extends ItemCardComposer{

    private BufferedImage usesGlyph = getImageFromFile("resources/glyphs/uses.png");
    private int uses;
    protected OneLineTextPane usesTextPane;

    public int getUses(){
        return uses;
    }

    public ConsumableCardComposer(){
        super(GlobalVar.CONSUMABLE);

        usesTextPane = new OneLineTextPane(OneLineTextPane.TYPE, 200, 600,935,100,60);
        usesTextPane.setOpaque(false);
        usesTextPane.setForeground(Color.WHITE);
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        Graphics2D g2d = finalImage.createGraphics();

        if (usesGlyph != null) {
            g2d.drawImage(usesGlyph, (int)(570*scale), (int)(930*scale),  (int)(160*scale),  (int)(70*scale), null);
        }

        BufferedImage uses = new BufferedImage((int)(100*scale),  (int)(60*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = uses.createGraphics();
        usesTextPane.setSize((int)(100 * scale), (int)(60 * scale));
        usesTextPane.doLayout();
        usesTextPane.validate();
        usesTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        g2d.drawImage(uses, (int) (600*scale), (int)(935*scale),  (int)(100*scale),  (int)(60*scale), null);
        g2d.dispose();
        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        super.setField(field, path);
        EventBus.publish(new RepaintPanelEvent());
    }

    
}
