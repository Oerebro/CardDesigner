package gui.controlpanel1;

//import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.awt.Font; 

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;

import gui.CardDesignerGUI;
import abstractclasses.*;
import events.EventBus;
import events.ImageUpdateEvent;
import events.InfoFontUpdate;
import events.InfoTextUpdate;
import events.SelectTypePanelUpdateEvent;

public class ControlPanel1 extends ControlPanel {

    //private JLabel frameLabel, backgroundLabel, textboxLabel;
    //private JButton loadFrameButton, loadBackgroundButton, loadTextboxButton;
    private CardDesignerGUI parent;
    
    private SelectTypePanel selectItemTypePanel;

    private JTabbedPane cardComponentTabbedPane;
    private ImageBrowser frameSelect, backgroundSelect, textboxSelect, titleSelect,effectSelect;
    private VariableTabbedPane selectItemArt, weapons,rune, armor, accessoire, consumable,effect,character;
    private JTextField titleTextField;
    private JTextArea infoTextField;
    private JComboBox<String> titleFontSelection,infoFontSelection;
    private JCheckBox titleStroke;

    public void init(CardDesignerGUI parent) {
        EventBus.subscribe(SelectTypePanelUpdateEvent.class, this::onTypeUpdate);
        setLayout(null);
        this.parent = parent;
        createButtons();
        compose();
        rescale(1.0);
    }

    public void setRangeAndACFont(String font){
        selectItemTypePanel.setRangeAndACFont(font);
    }

    private void createButtons() {
        createCardComponentSelection();
        selectItemTypePanel = new SelectTypePanel(parent);
    
        // Item Art VariableTabbedPane with default state weapons
        weapons = new VariableTabbedPane();
        weapons.init("weapon");
        selectItemArt = weapons;
    
        // Create title input field and font selector
        createTitleFontSelection();
        createInfoFontSelection();
        createTextFieldsAndPreview();
        
    }

    public String getTitleFont(){
        return (String) titleFontSelection.getSelectedItem();
    }

    public Boolean getTitleStroke(){
        return titleStroke.isSelected();
    }

    public void compose() {
        add(cardComponentTabbedPane);
        add(selectItemTypePanel);
        add(selectItemArt);
        add(titleFontSelection);
        add(infoFontSelection);
    }

    private void createTextFieldsAndPreview() {
        titleTextField = new JTextField();
        //titleTextField.setBounds(10, 290, 260, 30);
        add(titleTextField);

        infoTextField = new JTextArea();
        infoTextField.setPreferredSize(new java.awt.Dimension(485, 320));
        infoTextField.setLineWrap(true);
        infoTextField.setWrapStyleWord(true);
        infoTextField.setBorder(UIManager.getBorder("TextField.border"));
        add(infoTextField);

        ColorPicker titleColor = new ColorPicker(parent, 420, 290, 30, 30, "title");
        ColorPicker infoColor = new ColorPicker(parent, 420, 335, 30, 30, "info");

        titleStroke = new JCheckBox("Title Outline",false);
        titleStroke.setBounds(460,290,100,30);
        add(titleStroke);
        add(titleColor);
        add(infoColor);

    
        // Listen for text changes
        titleTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTitlePreview();
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTitlePreview();
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTitlePreview();
            }
        });

        infoTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateInfoPreview();
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateInfoPreview();
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateInfoPreview();
            }
        });
    
        titleFontSelection.addActionListener(e -> updateTitlePreview());
        infoFontSelection.addActionListener(e -> updateInfoPreview());
        
    }
    
    private void updateTitlePreview() {
        
        String text = titleTextField.getText();
        String selectedFontName = (String) titleFontSelection.getSelectedItem();
        Font font = new Font(selectedFontName, Font.PLAIN, 72);
        
        parent.updateTitleTextDisplay(text, font);
        parent.setRangeAndACFont(selectedFontName);
    }

    private void updateInfoPreview() {
        String text = infoTextField.getText();
        String selectedFontName = (String) infoFontSelection.getSelectedItem();
        Font font = new Font((String) infoFontSelection.getSelectedItem(), Font.PLAIN, 20); 
        EventBus.publish(new InfoFontUpdate(new Font((String) infoFontSelection.getSelectedItem(), Font.PLAIN, 20)));
        EventBus.publish(new InfoTextUpdate(text));
        
    }

    //this creates a dropdown menu for fonts next to the title text input
    private void createTitleFontSelection(){
        titleFontSelection = new JComboBox<>();
        Map<String,Font> fonts = loadFonts();

        for (String fontName : fonts.keySet()) {
            titleFontSelection.addItem(fontName);
        }
    }

    private void createInfoFontSelection(){
        infoFontSelection = new JComboBox<>();
        Map<String,Font> fonts = loadFonts();

        for (String fontName : fonts.keySet()) {
            infoFontSelection.addItem(fontName);
        }

        EventBus.publish(new InfoFontUpdate(new Font((String) infoFontSelection.getSelectedItem(), Font.PLAIN, 20)));
    }

    //this gets all fonts in folder and returns a map for the dropdown menu
    private Map<String, Font> loadFonts() {
        Map<String, Font> fonts = new HashMap<>();
        File fontFolder = new File("resources/misc/fonts");
        if (fontFolder.exists() && fontFolder.isDirectory()) {
            File[] files = fontFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".ttf") || name.toLowerCase().endsWith(".otf"));
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

        frameSelect = new CardImageBrowser("resources/card_components/frame",360,90,600,40, 64,"cardFrame");
        backgroundSelect = new CardImageBrowser("resources/card_components/background",360,90,600,40, 64,"cardBackground"); 
        textboxSelect = new CardImageBrowser("resources/card_components/textbox",360,90,600,40, 64,"cardTextbox");
        titleSelect = new CardImageBrowser("resources/card_components/title",360,90,600,40, 64,"cardTitle");

        effectSelect = new CardImageBrowser("resources/card_components/effects",360,90,600,40, 64,"cardBackground");

        cardComponentTabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Title",titleSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Effect Background",effectSelect.getScrollPane());
    }

    public void onTypeUpdate(SelectTypePanelUpdateEvent e){
        itemArtChangeToType(e.type);
    }

    private void itemArtChangeToType(String type){
        System.out.println("ControlPanel1::itemArtChangeToType");
        EventBus.publish(new ImageUpdateEvent("cardType","resources/"+type+".png"));
        remove(selectItemArt);

        switch(type){
            case "weapon": if(weapons == null) {weapons = new VariableTabbedPane(); weapons.init(type);} selectItemArt = weapons;   break;
            case "armor": if(armor == null) {armor = new VariableTabbedPane(); armor.init(type);} selectItemArt = armor;   break;
            case "accessoire": if(accessoire == null) {accessoire = new VariableTabbedPane(); accessoire.init(type);} selectItemArt = accessoire;   break;
            case "consumable": if(consumable == null) {consumable = new VariableTabbedPane(); consumable.init(type);} selectItemArt = consumable;   break;
            case "rune": if(rune == null) {rune = new VariableTabbedPane(); rune.init(type);} selectItemArt = rune;   break;
            //case "effect":
            //case "character":
        }

        add(selectItemArt);
        double scale = parent.getFrameScale();
        selectItemArt.setBounds((int) (390 *scale), (int) (0), (int) (360*scale), (int) (245*scale));
        selectItemArt.revalidate();
        selectItemArt.repaint();
    }



    public String getItemArtType(){
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
        selectItemTypePanel.setBounds((int) (770 *scale), (int) (0), (int) (300*scale), (int) (500*scale));

        titleFontSelection.setBounds((int) (320 *scale), (int) (290*scale), (int) (90*scale), (int) (30*scale));
        infoFontSelection.setBounds((int) (320 *scale), (int) (335*scale), (int) (90*scale), (int) (30*scale));
        titleTextField.setBounds((int) (10 *scale), (int) (290*scale), (int) (305*scale), (int) (30*scale));
        infoTextField.setBounds((int) (10 *scale), (int) (335*scale), (int) (305*scale), (int) (205*scale));


        //absolute pos of the controlpanel within the window frame
        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1210 * scale), (int)(1070 * scale));
    }
}
