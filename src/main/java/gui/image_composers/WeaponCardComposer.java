package gui.image_composers;

import java.awt.image.BufferedImage;

import events.CardLoadEvent;
import gui.card_types.*;

import java.awt.*;

public class WeaponCardComposer extends EquippableCardComposer{

    private BufferedImage diceImage, attributeImage, rangeTypeImage;

    private int dice, attribute;


    public int getDice(){
        return dice;
    }

    public int getAttribute(){
        return attribute;
    }

    public WeaponCardComposer(int type){
        super(type);
    }

    @Override
    public BufferedImage composeCard(double scale){
        BufferedImage finalImage = super.composeCard(scale);

        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        Graphics2D g2d = finalImage.createGraphics();


        if (rangeTypeImage != null) {
            g2d.drawImage(rangeTypeImage, 0, 0, targetWidth, targetHeight, null);
        }

        if (attributeImage != null) {
            g2d.drawImage(attributeImage, 0, 0, targetWidth, targetHeight, null);
        }

        if (diceImage != null) {
            g2d.drawImage(diceImage, 0, 0, targetWidth, targetHeight, null);
        }

        if (tierGlyph != null) {
            g2d.drawImage(tierGlyph, (int)(530*scale), (int)(465*scale),  (int)(180*scale),  (int)(180*scale), null);
        }

        return finalImage;
    }


    @Override
    protected void setField(int field, String path){
        switch(field){
            case Card.DICE: diceImage = getImageFromFile(path); dice = getDice(path); break;
            case Card.ATTRIBUTE: attributeImage = getImageFromFile(path); attribute = getAttribute(path);
            default: super.setField(field, path);
        }
    }

    private int getDice(String path){
        path = path.replaceAll("resources/glyphs/dice/d", "").replaceAll(".png", "");
        return Integer.parseInt(path);
    }

    private int getAttribute(String path){
        path = path.replaceAll("resources/glyphs/attributes/", path).replaceAll(".png", "");

        switch(path){
            case "strength": return Card.STRENGTH;
            case "constitution": return Card.CONSTITUTION;
            case "dexterity": return Card.DEXTERITY;
            case "wisdom": return Card.WISDOM;
            case "intelligence": return Card.INTELLIGENCE;
            case "charisma": return Card.CHARISMA;
        }

        return Card.STRENGTH;
    }

    @Override
    protected void onLoadCard(CardLoadEvent e) {
        super.onLoadCard(e);
    }


    
}
