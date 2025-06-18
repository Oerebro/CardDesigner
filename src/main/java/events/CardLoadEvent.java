package events;

import java.awt.Color;

public class CardLoadEvent {
    public String frameImage;
    public String backgroundImage;
    public String titleText,rangeText,infoText, typeText;
    public Color titleColor, infoColor = Color.WHITE;
    public int runeCharges, tier, uses, dice, ac, type, attribute,rangeType;

    //default
    public CardLoadEvent() {
        frameImage = "resources/img/card_components/frame/default.png";
        backgroundImage = "resources/img/card_components/background/default.png";
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