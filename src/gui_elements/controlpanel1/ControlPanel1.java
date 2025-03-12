package gui_elements.controlpanel1;

import java.awt.Dimension;
//import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.awt.Font; 

//import javax.imageio.ImageIO;
import javax.swing.*;

import abstractclasses.*;
import gui_elements.CardDesignerGUI;

public class ControlPanel1 extends ControlPanel {

    //private JLabel frameLabel, backgroundLabel, textboxLabel;
    //private JButton loadFrameButton, loadBackgroundButton, loadTextboxButton;
    private CardDesignerGUI parent;
    
    private SelectTypePanel selectItemTypePanel;

    private JTabbedPane cardComponentTabbedPane;
    private ImageBrowser frameSelect, backgroundSelect, textboxSelect, titleSelect;
    private VariableTabbedPane selectItemArt, weapons,weaponsTwoHanded, armor, accessoire, consumable;
    private JTextField titleTextField;
    private JComboBox<String> titleFontSelection;

    public void init(CardDesignerGUI parent) {
        setLayout(null);
        this.parent = parent;
        createButtons();
        compose();
        rescale(1.0);
    }

    private void createButtons() {
        createCardComponentSelection();
        selectItemTypePanel = new SelectTypePanel(parent);

        //Item Art VariableTabbedPane with default state weapons
        weapons = new VariableTabbedPane();
        weapons.init(parent,0);
        selectItemArt = weapons;    

        //create the text fields and font selectors
        createTitleTextFieldAndPreview();

        createTitleFontSelection();
    }

    public void compose() {
        add(cardComponentTabbedPane);
        add(selectItemTypePanel);
        add(selectItemArt);
        add(titleFontSelection);
    }

    private void  createTitleTextFieldAndPreview(){
        titleTextField = new JTextField();

    }

    private void createTitleFontSelection(){
        titleFontSelection = new JComboBox<>();
        Map<String,Font> fonts = loadFonts();

        for (String fontName : fonts.keySet()) {
            titleFontSelection.addItem(fontName);
        }
    }

    private Map<String, Font> loadFonts() {
        Map<String, Font> fonts = new HashMap<>();
        File fontFolder = new File("resources/misc/fonts");
        if (fontFolder.exists() && fontFolder.isDirectory()) {
            File[] files = fontFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".ttf"));
            if (files != null) {
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, fis).deriveFont(24f);
                        fonts.put(file.getName(), font);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return fonts;
    }

    private void createCardComponentSelection(){
        cardComponentTabbedPane = new JTabbedPane();

        frameSelect = new CardImageBrowser(parent,"resources/frame",360,90,600,40, 64,'f');
        backgroundSelect = new CardImageBrowser(parent,"resources/background",360,90,600,40, 64,'b'); 
        textboxSelect = new CardImageBrowser(parent,"resources/textbox",360,90,600,40, 64,'t');
        titleSelect = new CardImageBrowser(parent,"resources/title",360,90,600,40, 64,'h');

        cardComponentTabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Title",titleSelect.getScrollPane());
    }

    public void itemArtChangeToType(int type){
        remove(selectItemArt);

        if(!(type==0||type==10)){
            parent.setCardHandedImage(null);
        }

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
            case 10:{
                if(weaponsTwoHanded==null){
                    weaponsTwoHanded = new VariableTabbedPane();
                    weaponsTwoHanded.init(parent,10);
                }
                selectItemArt = weaponsTwoHanded;
            }break;
        }
        add(selectItemArt);
        double scale = parent.getFrameScale();
        selectItemArt.setBounds((int) (390 *scale), (int) (0), (int) (360*scale), (int) (245*scale));
        selectItemArt.revalidate();
        selectItemArt.repaint();
    }



    public int getItemArtType(){
        return selectItemArt.getType();
    }

    public void rescale(double scale) {
        //these rescales should be done in a better way, but they work atm
        frameSelect.rescale(scale);
        backgroundSelect.rescale(scale);
        textboxSelect.rescale(scale);

        //set the absolute position of these menus within the controlpanel
        cardComponentTabbedPane.setBounds((int)(10 * scale), (int)(0), (int)(360 * scale), (int)(245 * scale));   
        selectItemArt.setBounds((int) (390 *scale), (int) (0), (int) (360*scale), (int) (245*scale));
        selectItemTypePanel.setBounds((int) (770 *scale), (int) (0), (int) (300*scale), (int) (160*scale));


        //absolute pos of the controlpanel within the window frame
        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1210 * scale), (int)(1070 * scale));
    }
}
