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
        add("Axes",new ItemArtImageBrowser("resources/img/weapons/axes",440,350,1300,40,80).getScrollPane());
        add("Clubs",new ItemArtImageBrowser("resources/img/weapons/clubs",440,350,1300,40,80).getScrollPane());
        add("Curved Swords",new ItemArtImageBrowser("resources/img/weapons/curvedSwords",440,350,1300,40,80).getScrollPane());
        add("Daggers",new ItemArtImageBrowser("resources/img/weapons/daggers",440,350,1300,40,80).getScrollPane());
        add("Katanas",new ItemArtImageBrowser("resources/img/weapons/katanas",440,350,1300,40,80).getScrollPane());
        add("Maces",new ItemArtImageBrowser("resources/img/weapons/maces",440,350,1300,40,80).getScrollPane());
        add("Rapiers",new ItemArtImageBrowser("resources/img/weapons/rapiers",440,350,1300,40,80).getScrollPane());
        add("Straight Swords",new ItemArtImageBrowser("resources/img/weapons/straightSwords",440,350,1300,40,80).getScrollPane());
        add("Wands",new ItemArtImageBrowser("resources/img/weapons/wands",440,350,1300,40,80).getScrollPane());
        add("Bows",new ItemArtImageBrowser("resources/img/weapons/bows",440,350,1300,40,80).getScrollPane());
        add("Double Daggers",new ItemArtImageBrowser("resources/img/weapons/doubleDaggers",440,350,1300,40,80).getScrollPane());
        add("GreatAxes",new ItemArtImageBrowser("resources/img/weapons/greatAxes",440,350,1300,40,80).getScrollPane());
        add("Greatclubs",new ItemArtImageBrowser("resources/img/weapons/greatClubs",440,350,1300,40,80).getScrollPane());
        add("Greathammers",new ItemArtImageBrowser("resources/img/weapons/greatHammers",440,350,1300,40,80).getScrollPane());
        add("Greatswords",new ItemArtImageBrowser("resources/img/weapons/greatSwords",440,350,1300,40,80).getScrollPane());
        add("Halberds",new ItemArtImageBrowser("resources/img/weapons/halberds",440,350,1300,40,80).getScrollPane());
        add("Hammers",new ItemArtImageBrowser("resources/img/weapons/hammers",440,350,1300,40,80).getScrollPane());
        add("Scythes",new ItemArtImageBrowser("resources/img/weapons/scythes",440,350,1300,40,80).getScrollPane());
        add("Spears",new ItemArtImageBrowser("resources/img/weapons/spears",440,350,1300,40,80).getScrollPane());
        add("Staves",new ItemArtImageBrowser("resources/img/weapons/staves",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsRune(){
        add("Offensive",new ItemArtImageBrowser("resources/img/runes/offensive",440,350,1300,40,80).getScrollPane());
        add("Defensive",new ItemArtImageBrowser("resources/img/runes/defensive",440,350,1300,40,80).getScrollPane());
        add("Support",new ItemArtImageBrowser("resources/img/runes/utility",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsArmor(){
        add("Chest",new ItemArtImageBrowser("resources/img/armor/armor",440,350,1300,40,80).getScrollPane());
        add("Helmet",new ItemArtImageBrowser("resources/img/armor/helmet",440,350,1300,40,80).getScrollPane());
        add("Glove",new ItemArtImageBrowser("resources/img/armor/glove",440,350,1300,40,80).getScrollPane());
        add("Pants",new ItemArtImageBrowser("resources/img/armor/pants",440,350,1300,40,80).getScrollPane());
        add("Capes",new ItemArtImageBrowser("resources/img/armor/cape",440,350,1300,40,80).getScrollPane());
        add("Shields",new ItemArtImageBrowser("resources/img/armor/shield",440,350,1300,40,80).getScrollPane());
        

    }

    private void addTabsAccessoire(){
        add("Rings",new ItemArtImageBrowser("resources/img/armor/ring",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsConsumable(){
        add("Elixirs",new ItemArtImageBrowser("resources/img/consumable/elixir",440,350,1300,40,80).getScrollPane());
        add("Potions",new ItemArtImageBrowser("resources/img/consumable/potion",440,350,1300,40,80).getScrollPane());
        add("Weaponoils",new ItemArtImageBrowser("resources/img/consumable/oil",440,350,1300,40,80).getScrollPane());
        add("Bombs",new ItemArtImageBrowser("resources/img/consumable/grenade",440,350,1300,40,80).getScrollPane());
        add("Throwable Flasks",new ItemArtImageBrowser("resources/img/consumable/throwableFlask",440,350,1300,40,80).getScrollPane());
        
    }
    private void addTabsCharacters(){
        //add("Negative",new ItemArtImageBrowser("resources/img/effects/negative",440,350,1300,40,80).getScrollPane());
        
    }
}
