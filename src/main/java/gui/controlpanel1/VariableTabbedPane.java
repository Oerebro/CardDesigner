package gui.controlpanel1;

import javax.swing.JTabbedPane;

public class VariableTabbedPane extends JTabbedPane{
    /*types: 
    0 - weaponOneHand
    10 - weaponTwoHand
    1 - armor
    2 - clothing
    3 - accessoire
    4 - consumable
    */
    private String type;


    public void init(String type){
        this.type = type;
        switchToType(type);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public String getType(){
        return type;
    }
    
    public void switchToType(String type){
        switch(type){
            case "weapon": addTabsWeaponOneHand();break;
            case "rune": addTabsRune();break;
            case "armor": addTabsArmor();break;
            case "accessoire": addTabsAccessoire();break;
            case "consumable": addTabsConsumable();break;
            case "character": addTabsCharacters();break;
        }


    }

    private void addTabsWeaponOneHand(){
        add("Axes",new ItemArtImageBrowser("resources/weapons/axes",440,350,1300,40,80).getScrollPane());
        add("Clubs",new ItemArtImageBrowser("resources/weapons/clubs",440,350,1300,40,80).getScrollPane());
        add("Curved Swords",new ItemArtImageBrowser("resources/weapons/curvedSwords",440,350,1300,40,80).getScrollPane());
        add("Daggers",new ItemArtImageBrowser("resources/weapons/daggers",440,350,1300,40,80).getScrollPane());
        add("Katanas",new ItemArtImageBrowser("resources/weapons/katanas",440,350,1300,40,80).getScrollPane());
        add("Maces",new ItemArtImageBrowser("resources/weapons/maces",440,350,1300,40,80).getScrollPane());
        add("Rapiers",new ItemArtImageBrowser("resources/weapons/rapiers",440,350,1300,40,80).getScrollPane());
        add("Straight Swords",new ItemArtImageBrowser("resources/weapons/straightSwords",440,350,1300,40,80).getScrollPane());
        add("Wands",new ItemArtImageBrowser("resources/weapons/wands",440,350,1300,40,80).getScrollPane());
        add("Bows",new ItemArtImageBrowser("resources/weapons/bows",440,350,1300,40,80).getScrollPane());
        add("Double Daggers",new ItemArtImageBrowser("resources/weapons/doubleDaggers",440,350,1300,40,80).getScrollPane());
        add("GreatAxes",new ItemArtImageBrowser("resources/weapons/greatAxes",440,350,1300,40,80).getScrollPane());
        add("Greatclubs",new ItemArtImageBrowser("resources/weapons/greatClubs",440,350,1300,40,80).getScrollPane());
        add("Greathammers",new ItemArtImageBrowser("resources/weapons/greatHammers",440,350,1300,40,80).getScrollPane());
        add("Greatswords",new ItemArtImageBrowser("resources/weapons/greatSwords",440,350,1300,40,80).getScrollPane());
        add("Halberds",new ItemArtImageBrowser("resources/weapons/halberds",440,350,1300,40,80).getScrollPane());
        add("Hammers",new ItemArtImageBrowser("resources/weapons/hammers",440,350,1300,40,80).getScrollPane());
        add("Scythes",new ItemArtImageBrowser("resources/weapons/scythes",440,350,1300,40,80).getScrollPane());
        add("Spears",new ItemArtImageBrowser("resources/weapons/spears",440,350,1300,40,80).getScrollPane());
        add("Staves",new ItemArtImageBrowser("resources/weapons/staves",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsRune(){
        add("Offensive",new ItemArtImageBrowser("resources/runes/offensive",440,350,1300,40,80).getScrollPane());
        add("Defensive",new ItemArtImageBrowser("resources/runes/defensive",440,350,1300,40,80).getScrollPane());
        add("Support",new ItemArtImageBrowser("resources/runes/utility",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsArmor(){
        add("Chest",new ItemArtImageBrowser("resources/armor/armor",440,350,1300,40,80).getScrollPane());
        add("Helmet",new ItemArtImageBrowser("resources/armor/helmet",440,350,1300,40,80).getScrollPane());
        add("Glove",new ItemArtImageBrowser("resources/armor/glove",440,350,1300,40,80).getScrollPane());
        add("Pants",new ItemArtImageBrowser("resources/armor/pants",440,350,1300,40,80).getScrollPane());
        add("Capes",new ItemArtImageBrowser("resources/armor/cape",440,350,1300,40,80).getScrollPane());
        add("Shields",new ItemArtImageBrowser("resources/armor/shield",440,350,1300,40,80).getScrollPane());
        

    }

    private void addTabsAccessoire(){
        add("Rings",new ItemArtImageBrowser("resources/armor/ring",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsConsumable(){
        add("Elixirs",new ItemArtImageBrowser("resources/consumable/elixir",440,350,1300,40,80).getScrollPane());
        add("Potions",new ItemArtImageBrowser("resources/consumable/potion",440,350,1300,40,80).getScrollPane());
        add("Weaponoils",new ItemArtImageBrowser("resources/consumable/oil",440,350,1300,40,80).getScrollPane());
        add("Bombs",new ItemArtImageBrowser("resources/consumable/grenade",440,350,1300,40,80).getScrollPane());
        add("Throwable Flasks",new ItemArtImageBrowser("resources/consumable/throwableFlask",440,350,1300,40,80).getScrollPane());
        
    }
    private void addTabsCharacters(){
        //add("Negative",new ItemArtImageBrowser("resources/effects/negative",440,350,1300,40,80).getScrollPane());
        
    }
}
