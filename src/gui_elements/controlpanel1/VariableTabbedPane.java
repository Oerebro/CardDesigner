package gui_elements.controlpanel1;

import javax.swing.JTabbedPane;

import gui_elements.CardDesignerGUI;

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


    public void init(CardDesignerGUI parent){
        this.parent = parent;
        switchToType(0);
    }

    public int getType(){
        return type;
    }
    
    public void switchToType(int type){
        removeAll();
        this.type = type;
        
        switch(type){
            case 0: addTabsWeaponOneHand();break;
            case 10: addTabsWeaponTwoHand();break;
            case 1: addTabsArmor();break;
            case 3: addTabsAccessoire();break;
            case 4: addTabsConsumable();break;
        }


    }

    private void addTabsWeaponOneHand(){
        add("Axes",new ItemArtImageBrowser(parent,"resources/weapons/axes",440,350,'7').getScrollPane());
        add("Clubs",new ItemArtImageBrowser(parent,"resources/weapons/clubs",440,350,'7').getScrollPane());
        add("Curved Swords",new ItemArtImageBrowser(parent,"resources/weapons/clubs",440,350,'7').getScrollPane());
        add("Daggers",new ItemArtImageBrowser(parent,"resources/weapons/daggers",440,350,'7').getScrollPane());
        add("Hammers",new ItemArtImageBrowser(parent,"resources/weapons/hammers",440,350,'7').getScrollPane());
        add("Katanas",new ItemArtImageBrowser(parent,"resources/weapons/katanas",440,350,'7').getScrollPane());
        add("Maces",new ItemArtImageBrowser(parent,"resources/weapons/maces",440,350,'7').getScrollPane());
        add("Rapiers",new ItemArtImageBrowser(parent,"resources/weapons/rapiers",440,350,'7').getScrollPane());
        add("Straight Swords",new ItemArtImageBrowser(parent,"resources/weapons/straightSwords",350,350,'7').getScrollPane());
        add("Wands",new ItemArtImageBrowser(parent,"resources/weapons/wands",440,440,'7').getScrollPane());
    }

    private void addTabsWeaponTwoHand(){
        add("Bows",new ItemArtImageBrowser(parent,"resources/weapons/bows",744020,350,'7').getScrollPane());
        add("Curved Greatswords",new ItemArtImageBrowser(parent,"resources/weapons/curvedGreatSwords",440,350,'7').getScrollPane());
        add("Double Daggers",new ItemArtImageBrowser(parent,"resources/weapons/doubleDaggers",440,350,'7').getScrollPane());
        add("GreatAxes",new ItemArtImageBrowser(parent,"resources/weapons/greatAxes",440,350,'7').getScrollPane());
        add("Greatclubs",new ItemArtImageBrowser(parent,"resources/weapons/greatClubs",440,350,'7').getScrollPane());
        add("Greathammers",new ItemArtImageBrowser(parent,"resources/weapons/greatHammers",440,350,'7').getScrollPane());
        add("Greatswords",new ItemArtImageBrowser(parent,"resources/weapons/greatSwords",440,350,'7').getScrollPane());
        add("Halberds",new ItemArtImageBrowser(parent,"resources/weapons/halberds",440,350,'7').getScrollPane());
        add("Scythes",new ItemArtImageBrowser(parent,"resources/weapons/scythes",440,350,'7').getScrollPane());
        add("Spears",new ItemArtImageBrowser(parent,"resources/weapons/spears",440,350,'7').getScrollPane());
        add("Staves",new ItemArtImageBrowser(parent,"resources/weapons/spears",440,350,'7').getScrollPane());
    }

    private void addTabsArmor(){
        add("Chest",new ItemArtImageBrowser(parent,"resources/armor/armor",440,350,'7').getScrollPane());
        add("Helmet",new ItemArtImageBrowser(parent,"resources/armor/helmet",440,350,'7').getScrollPane());
        add("Glove",new ItemArtImageBrowser(parent,"resources/armor/glove",440,350,'7').getScrollPane());
        add("Pants",new ItemArtImageBrowser(parent,"resources/armor/pants",440,350,'7').getScrollPane());
        add("Shields",new ItemArtImageBrowser(parent,"resources/armor/shield",440,350,'7').getScrollPane());
        

    }

    private void addTabsClothing(){

    }

    private void addTabsAccessoire(){
    }

    private void addTabsConsumable(){
    }
}
