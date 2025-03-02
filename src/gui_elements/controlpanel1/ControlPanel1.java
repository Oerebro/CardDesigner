package gui_elements.controlpanel1;

import java.awt.Dimension;
//import java.io.File;

//import javax.imageio.ImageIO;
import javax.swing.*;

import abstractclasses.*;
import gui_elements.CardDesignerGUI;

public class ControlPanel1 extends ControlPanel {

    //private JLabel frameLabel, backgroundLabel, textboxLabel;
    //private JButton loadFrameButton, loadBackgroundButton, loadTextboxButton;
    private CardDesignerGUI parent;
    
    private SelectTypePanel selectItemTypePanel;

    private JTabbedPane tabbedPane;
    private ImageBrowser frameSelect, backgroundSelect, textboxSelect, titleSelect;
    private VariableTabbedPane selectItemArt, weapons, armor, accessoire, consumable;

    public void init(CardDesignerGUI parent) {
        this.parent = parent;
        createButtons();
        compose();
        rescale(1.0);
    }

    public void setItemArtType(int type){
        selectItemArt.switchToType(type);
    }

    

    private void createButtons() {
        // Create the dropdown menu for frame selection
        tabbedPane = new JTabbedPane();
        frameSelect = new CardImageBrowser(parent,"resources/frame",360,180,600,40, 64,'f');
        backgroundSelect = new CardImageBrowser(parent,"resources/background",360,180,600,40, 64,'b');
        
        textboxSelect = new CardImageBrowser(parent,"resources/textbox",360,180,600,40, 64,'t');
        titleSelect = new CardImageBrowser(parent,"resources/title",360,180,600,40, 64,'h');
        tabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        tabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        tabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        tabbedPane.addTab("Choose Title",titleSelect.getScrollPane());

        selectItemTypePanel = new SelectTypePanel(parent);

        weapons = new VariableTabbedPane();
        weapons.init(parent,0);
        selectItemArt = weapons;

        
    }

    public void changeToType(int type){
        selectItemArt.removeAll();
        switch(type){
            case 0:{
                selectItemArt = weapons;
            }break;
            case 1:{
                if(armor == null){
                    armor = new VariableTabbedPane();
                    armor.init(parent,1);
                }
                selectItemArt = armor;
            }break;
            case 3:{
                if(accessoire==null){
                    accessoire = new VariableTabbedPane();
                    accessoire.init(parent,3);
                }
                selectItemArt = accessoire;
            }break;
            case 4:{
                if(consumable==null){
                    consumable = new VariableTabbedPane();
                    consumable.init(parent,4);
                }
                selectItemArt = consumable;
            }break;
        }
        selectItemArt.revalidate();
        selectItemArt.repaint();
    }

    public void compose() {
        //System.out.println("Compose");
        add(tabbedPane);
        add(selectItemTypePanel.menu);
        add(selectItemArt);
    }

    public int getItemArtType(){
        return selectItemArt.getType();
    }

    public void rescale(double scale) {
        System.out.println("rescale");
        frameSelect.rescale(scale);
        backgroundSelect.rescale(scale);
        textboxSelect.rescale(scale);
        tabbedPane.setPreferredSize(new Dimension((int)(360 * scale), (int)(180 * scale)));
        tabbedPane.setBounds((int)(575 * scale), (int)(10 * scale), (int)(360 * scale), (int)(180 * scale));
        
        //tabbed Pane of the item art selector
        selectItemArt.setBounds((int) (980*scale), (int) (260*scale), (int) (360*scale), (int) (500*scale));



        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1100 * scale), (int)(1070 * scale));
    }
}
