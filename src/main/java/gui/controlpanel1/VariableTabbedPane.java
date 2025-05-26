package gui.controlpanel1;

import javax.swing.JTabbedPane;

import gui.CardDesignerGUI;

public class VariableTabbedPane extends JTabbedPane{
    /*types: 
    0 - weaponOneHand
    10 - weaponTwoHand
    1 - armor
    2 - clothing
    3 - accessoire
    4 - consumable
    */

    private CardDesignerGUI parent;
    private int type;


    public void init(CardDesignerGUI parent, int type){
        this.parent = parent;
        switchToType(type);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public int getType(){
        return type;
    }
    
    public void switchToType(int type){
        //removeAll();
        this.type = type;
        
        switch(type){
            case 0: addTabsWeaponOneHand();break;
            case 10: addTabsRune();break;
            case 1: addTabsArmor();break;
            case 3: addTabsAccessoire();break;
            case 4: addTabsConsumable();break;
            case 5: addTabsCharacters();break;
        }


    }

    private void addTabsWeaponOneHand(){
        add("Axes",new ItemArtImageBrowser(parent,"resources/weapons/axes",440,350,1300,40,80).getScrollPane());
        add("Clubs",new ItemArtImageBrowser(parent,"resources/weapons/clubs",440,350,1300,40,80).getScrollPane());
        add("Curved Swords",new ItemArtImageBrowser(parent,"resources/weapons/curvedSwords",440,350,1300,40,80).getScrollPane());
        add("Daggers",new ItemArtImageBrowser(parent,"resources/weapons/daggers",440,350,1300,40,80).getScrollPane());
        add("Katanas",new ItemArtImageBrowser(parent,"resources/weapons/katanas",440,350,1300,40,80).getScrollPane());
        add("Maces",new ItemArtImageBrowser(parent,"resources/weapons/maces",440,350,1300,40,80).getScrollPane());
        add("Rapiers",new ItemArtImageBrowser(parent,"resources/weapons/rapiers",440,350,1300,40,80).getScrollPane());
        add("Straight Swords",new ItemArtImageBrowser(parent,"resources/weapons/straightSwords",440,350,1300,40,80).getScrollPane());
        add("Wands",new ItemArtImageBrowser(parent,"resources/weapons/wands",440,350,1300,40,80).getScrollPane());
        add("Bows",new ItemArtImageBrowser(parent,"resources/weapons/bows",440,350,1300,40,80).getScrollPane());
        add("Double Daggers",new ItemArtImageBrowser(parent,"resources/weapons/doubleDaggers",440,350,1300,40,80).getScrollPane());
        add("GreatAxes",new ItemArtImageBrowser(parent,"resources/weapons/greatAxes",440,350,1300,40,80).getScrollPane());
        add("Greatclubs",new ItemArtImageBrowser(parent,"resources/weapons/greatClubs",440,350,1300,40,80).getScrollPane());
        add("Greathammers",new ItemArtImageBrowser(parent,"resources/weapons/greatHammers",440,350,1300,40,80).getScrollPane());
        add("Greatswords",new ItemArtImageBrowser(parent,"resources/weapons/greatSwords",440,350,1300,40,80).getScrollPane());
        add("Halberds",new ItemArtImageBrowser(parent,"resources/weapons/halberds",440,350,1300,40,80).getScrollPane());
        add("Hammers",new ItemArtImageBrowser(parent,"resources/weapons/hammers",440,350,1300,40,80).getScrollPane());
        add("Scythes",new ItemArtImageBrowser(parent,"resources/weapons/scythes",440,350,1300,40,80).getScrollPane());
        add("Spears",new ItemArtImageBrowser(parent,"resources/weapons/spears",440,350,1300,40,80).getScrollPane());
        add("Staves",new ItemArtImageBrowser(parent,"resources/weapons/staves",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsRune(){
        add("Offensive",new ItemArtImageBrowser(parent,"resources/runes/offensive",440,350,1300,40,80).getScrollPane());
        add("Defensive",new ItemArtImageBrowser(parent,"resources/runes/defensive",440,350,1300,40,80).getScrollPane());
        add("Support",new ItemArtImageBrowser(parent,"resources/runes/utility",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsArmor(){
        add("Chest",new ItemArtImageBrowser(parent,"resources/armor/armor",440,350,1300,40,80).getScrollPane());
        add("Helmet",new ItemArtImageBrowser(parent,"resources/armor/helmet",440,350,1300,40,80).getScrollPane());
        add("Glove",new ItemArtImageBrowser(parent,"resources/armor/glove",440,350,1300,40,80).getScrollPane());
        add("Pants",new ItemArtImageBrowser(parent,"resources/armor/pants",440,350,1300,40,80).getScrollPane());
        add("Capes",new ItemArtImageBrowser(parent,"resources/armor/cape",440,350,1300,40,80).getScrollPane());
        add("Shields",new ItemArtImageBrowser(parent,"resources/armor/shield",440,350,1300,40,80).getScrollPane());
        

    }

    private void addTabsAccessoire(){
        add("Rings",new ItemArtImageBrowser(parent,"resources/armor/ring",440,350,1300,40,80).getScrollPane());
    }

    private void addTabsConsumable(){
        add("Elixirs",new ItemArtImageBrowser(parent,"resources/consumable/elixir",440,350,1300,40,80).getScrollPane());
        add("Potions",new ItemArtImageBrowser(parent,"resources/consumable/potion",440,350,1300,40,80).getScrollPane());
        add("Weaponoils",new ItemArtImageBrowser(parent,"resources/consumable/oil",440,350,1300,40,80).getScrollPane());
        add("Bombs",new ItemArtImageBrowser(parent,"resources/consumable/grenade",440,350,1300,40,80).getScrollPane());
        add("Throwable Flasks",new ItemArtImageBrowser(parent,"resources/consumable/throwableFlask",440,350,1300,40,80).getScrollPane());
        
    }
    private void addTabsCharacters(){
        //add("Negative",new ItemArtImageBrowser(parent,"resources/effects/negative",440,350,1300,40,80).getScrollPane());
        
    }
}
