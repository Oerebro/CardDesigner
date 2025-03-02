package abstractclasses;

import javax.swing.JTabbedPane;

import gui_elements.CardDesignerGUI;
import gui_elements.controlpanel1.ImageBrowser;

public class VariableTabbedPane extends JTabbedPane{
    /*types: 
    0 - weapon
    1 - armor
    2 - clothing
    3 - accessoire
    4 - consumable
    */

    public void init(CardDesignerGUI parent){
        this.parent = parent;
    }

    private int type;
    private CardDesignerGUI parent;

    public int getType(){
        return type;
    }
    
    public void switchToType(int type){
        this.type = type;


    }

    private void addTabsWeapon(){
        add(new ImageBrowser(parent,"resources/textbox",360,180,'7').filePanel)
    }

    private void addTabsArmor(){}

    private void addTabsClothing(){}

    private void addTabsAccessoire(){}

    private void addTabsConsumabl