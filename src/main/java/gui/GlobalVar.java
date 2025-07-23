package gui;

public class GlobalVar {
    // Block 1: General Types
    public static final int CHARACTER = 101;
    public static final int EFFECT = 102;
    public static final int WEAPON = 103;
    public static final int ARMOR = 104;
    public static final int CONSUMABLE = 105;
    public static final int RUNE = 106;
    public static final int ACCESSOIRE = 107;
    public static final int ARKHAM = 108;

    // Block 2: Weapon Types
    public static final int W_MELEE = 201;
    public static final int W_RANGED = 202;
    public static final int W_THROWABLE = 203;
    public static final int W_MAGIC = 204;

    // Block 3: Attributes
    public static final int STRENGTH = 301;
    public static final int CONSTITUTION = 302;
    public static final int DEXTERITY = 303;
    public static final int WISDOM = 304;
    public static final int INTELLIGENCE = 305;
    public static final int CHARISMA = 306;

    // Block 4: Armor Values
    public static final int ARMOR1 = 401;
    public static final int ARMOR2 = 402;

    // Block 5: Dice and Attribute Base
    public static final int DICE = 501;
    public static final int ATTRIBUTE_BASE = 502;

    // Block 6: Rune Data
    public static final int TIER = 601;
    public static final int RUNECHARGES = 602;
    public static final int RUNECUT = 603;

    // Block 7: Image Components
    public static final int ITEM_IMAGE = 701;
    public static final int FRAME_IMAGE = 702;
    public static final int BACKGROUND_IMAGE = 703;
    public static final int TITLE_IMAGE = 704;
    public static final int CROWN_IMAGE = 705;
    public static final int TEXTBOX_IMAGE = 706;

    // Block 8: Combat Stats
    public static final int RANGE_TYPE = 801;
    public static final int ATTRIBUTE = 802;
    public static final int DAMAGE_MELEE = 803;
    public static final int DAMAGE_RANGED = 804;
    public static final int DAMAGE_MAGIC = 805;

    // Block 9: Borders
    public static final int TITLE_BORDER = 901;
    public static final int INFO_BORDER = 902;
    public static final int TYPE_BORDER = 903;

    // Block 10: Event Subtypes
    public static final int INFO_TEXT_UPDATE = 1001;
    public static final int TITLE_TEXT_UPDATE = 1002;
    public static final int TYPE_TEXT_UPDATE = 1003;
    public static final int RANGE_NORMAL_TEXT_UPDATE = 1004;
    public static final int RANGE_MAX_TEXT_UPDATE = 1005;
    public static final int FONTSIZE_TEXT_UPDATE = 1006;
    public static final int FONTSIZE_FIELD_UPDATE = 1007;
    public static final int REPAINT_ALL = 1008;
    public static final int REPAINT_TITLE = 1009;
    public static final int REPAINT_INFO = 10010;
    public static final int REPAINT_ATTRIBUTE_LABEL = 10011;
    public static final int REPAINT_TIER_LABEL = 10012;
    public static final int REPAINT_RUNECHARGE_LABEL = 10013;
    public static final int REPAINT_TYPE = 10014;
    public static final int REPAINT_BACKGROUND = 10015;
    public static final int REPAINT_IMAGE = 10016;
    public static final int REPAINT_RUNECUT = 10017;
    public static final int REPAINT_FRAME = 10018;
    public static final int REPAINT_TEXTBOX = 10019;
    public static final int REPAINT_CROWN = 10020;
    public static final int OTHER_TEXT_UPDATE_1 = 10021;
    public static final int OTHER_TEXT_UPDATE_2 = 10022;
    public static final int ARKHAM_RANGE_IMAGE_UPDATE = 10023;
    public static final int ARKHAM_RANGE_IMAGE_UPDATE_NONE = 10024;
    public static final int ARKHAM_CARDSIDE_IMAGE_UPDATE = 10025;

    // Paths
    public static final String DICE_IMAGE_PATH = "resources/glyphs/dice/d";
    public static final String ATTRIBUTE_IMAGE_PATH = "resources/glyphs/attributes/";
    public static final String AC_IMAGE_PATH = "resources/glyphs/ac/";
    public static final String TIER_IMAGE_PATH = "resources/glyphs/tier/";
    public static final String GLYPH_PATH = "resources/glyphs/";
    public static final String RUNECHARGE_IMAGE_PATH = "resources/glyphs/runecharge/";
    public static final String ATTRIBUTE_LABEL_COMPONENTS = "resources/glyphs/AttributeLabel/";
    public static final String CARD_COMPONENTS_IMAGE_PATH = "resources/img/card_components/";

    public static final String CARD_SAVE_DIR = "saved/";
}
