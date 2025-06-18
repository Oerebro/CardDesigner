package gui.image_composers;

import java.awt.image.BufferedImage;

import events.CardLoadEvent;
import events.EventBus;
import events.InfoTextUpdate;
import events.ItemImageUpdateEvent;
import gui.card_types.*;
import gui.previewpanel.OneLineTextPane;

import java.awt.*;

public class ItemCardComposer extends CardComposer{

    protected BufferedImage cardItemImage;
    protected String cardItemImagePath;
    protected OneLineTextPane typeTextPane;

    

    public String getItemImagePath(){
        return cardItemImagePath;
    }

    public ItemCardComposer(int type){
        super(type);

        typeTextPane = new OneLineTextPane(OneLineTextPane.TYPE, 200, 80,25,590,80);
        typeTextPane.setOpaque(false);
        typeTextPane.setForeground(Color.WHITE);
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();

        if (cardItemImage != null) {
            g2d.drawImage(cardItemImage, 0, 0, targetWidth, targetHeight, null);
        }

        if(cardFrame != null){
            g2d.drawImage(cardFrame, 0, 0, targetWidth, targetHeight, null);
        }

        if (runeCut != null) {
            g2d.drawImage(runeCut, 0, 0, targetWidth, targetHeight, null);
        }

        int x,y,w,h;

        BufferedImage typeText = new BufferedImage((int)(670*scale),  (int)(60*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D labelGraphics = typeText.createGraphics();
        typeTextPane.setSize((int)(670 * scale), (int)(60 * scale));
        typeTextPane.doLayout();
        typeTextPane.validate();
        typeTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        g2d.drawImage(typeText, (int) (40*scale), (int)(590*scale),  (int)(670*scale),  (int)(60*scale), null);

        

        x = (int) (infoX * scale);
        y = (int) (infoY * scale);
        w = (int) (infoW * scale);
        h = (int) (infoH * scale);

        BufferedImage infoText = new BufferedImage(w,  h, BufferedImage.TYPE_INT_ARGB);
        labelGraphics = infoText.createGraphics();
        infoTextPane.setBounds(x, y, w,h);
        //infoTextPane.scaleFont();
        infoTextPane.doLayout();
        infoTextPane.revalidate();
        infoTextPane.repaint(); 
        infoTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        g2d.drawImage(infoText, x, y,  w,h, null);
        

        x = (int) (titleX * scale);
        y = (int) (titleY * scale);
        w = (int) (titleW * scale);
        h = (int) (titleH * scale);

        BufferedImage titleText = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        labelGraphics = titleText.createGraphics();
        titleTextPane.setSize(w,h);   
        titleTextPane.revalidate();
        titleTextPane.repaint(); 
        titleTextPane.printAll(labelGraphics);
        labelGraphics.dispose();
        if(titleBorder){
            titleText = drawStroke(titleText,3,Color.WHITE);
        }
        g2d.drawImage(titleText,x, y, w, h, null);

        return finalImage;
    }


    @Override
    protected void setField(int field, String path){

        switch(field){
            case Card.ITEM_IMAGE: cardItemImage = getImageFromFile(path); cardItemImagePath = path; break;
            default: super.setField(field, path);
        }

    }

    @Override
    protected void onImageUpdate(ItemImageUpdateEvent e) {

        super.onImageUpdate(e);

        setField(Card.ITEM_IMAGE, e.path);
        //EventBus.publish(new RepaintPanelEvent());
    }

    @Override
    protected void onLoadCard(CardLoadEvent e) {
        super.onLoadCard(e);
    }


    
}
