package gui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageComposerConfig {
    public String cardFrame;
    public String cardBackground;
    public String cardTextbox;
    public String cardTitle;
    public String cardItemImage;
    public String attributeImage;
    public String cardType;
    public String handedImage;
    public String tierGlyph;
    public String weaponType;
    
    public String runeCut;
    public String armorclass1;
    public String armorclass2;
    public String effectImage;

    public String titleFont;
    public String titleText;

    public String infoFont;
    public String infoText;

    public String type;
    public String range;
    public int ac;
    public int tier;
    public int dice;
    public int runeSlots;
    
    public Boolean isWeapon;
    public Boolean isArmor;
    public Boolean isClothing;
    public Boolean isAccessoire;
    public Boolean isConsumable;
    public Boolean isEffect;
    public Boolean isRune;
    public Boolean hasRuneCut;
    public Boolean hasTitleBorder;

}
