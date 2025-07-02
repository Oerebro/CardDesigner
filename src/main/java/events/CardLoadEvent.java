package events;

import java.awt.Color;

import gui.GlobalVar;

public class CardLoadEvent {
    public String frameImage, backgroundImage, titleImage, crownImage, textBoxImage;
    public String titleText,rangeText,infoText, typeText;
    public Color titleColor, infoColor = Color.WHITE;
    public int runeCharges, tier, uses, dice, ac, type, attribute,rangeType;

    //default
    public CardLoadEvent() {
        frameImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"frame/default.png";
        backgroundImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"background/default.png";
        titleImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"title/default.png";
        crownImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"crowns/default.png";
        textBoxImage = GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"textbox/default.png";
        titleText = "";
        rangeText = "";
        infoText = "";
        typeText = "";
    }

    //Melee Weapon
    public CardLoadEvent(String frameImage, String backgroundImage, String titleText, String infoText, String typeText, int runeCharges, int tier, int dice, int attribute, int type){
        this.frameImage = frameImage;
        this.backgroundImage = backgroundImage;
        this.titleText = titleText;
        this.infoText = infoText;
        this.typeText = typeText;
        this.runeCharges = runeCharges;
        this.tier = tier;
        this.type = type;

        this.dice = dice;
        this.attribute = attribute;
    }

    //Ranged/Throwable
    public CardLoadEvent(String frameImage, String backgroundImage, String titleText, String rangeText, String infoText, String typeText, int runeCharges, int tier, int dice, int attribute, int type){
        this.frameImage = frameImage;
        this.backgroundImage = backgroundImage;
        this.titleText = titleText;
        this.infoText = infoText;
        this.typeText = typeText;
        this.runeCharges = runeCharges;
        this.tier = tier;
        this.type = type;
        this.rangeText = rangeText;
        this.dice = dice;
        this.attribute = attribute;
    }

    //Armor
    public CardLoadEvent(String frameImage, String backgroundImage, String titleText, String infoText, String typeText, int runeCharges, int tier, int ac, int type){
        this.frameImage = frameImage;
        this.backgroundImage = backgroundImage;
        this.titleText = titleText;
        this.infoText = infoText;
        this.typeText = typeText;
        this.runeCharges = runeCharges;
        this.tier = tier;
        this.type = type;

        this.ac = ac;

    }
}