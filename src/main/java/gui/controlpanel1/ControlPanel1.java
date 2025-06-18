package gui.controlpanel1;

//import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Font; 

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;

import gui.CardDesignerGUI;
import gui.ImageComposerConfig;
import gui.card_types.Card;
import abstractclasses.*;
import events.GetCardAttributesEvent;
import events.EventBus;
import events.ItemImageUpdateEvent;
import events.InfoFontUpdate;
import events.InfoTextUpdate;
import events.LoadConfigEvent;
import events.RepaintPanelEvent;
import events.SelectTypePanelUpdateEvent;
import events.TitleFontUpdate;
import events.TitleTextUpdate;
import events.ToggleTitleBorder;
import gui.controlpanel1.FontLoader;

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
        EventBus.subscribe(LoadConfigEvent.class, this::onLoadConfig);
        
        
        setLayout(null);
        this.parent = parent;
        createButtons();
        compose();
        rescale(1.0);
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
        titleStroke.addActionListener(e->{EventBus.publish(new ToggleTitleBorder(titleStroke.isSelected()));});
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
        //Font font = new Font(selectedFontName, Font.PLAIN, 72);
        
        EventBus.publish(new TitleFontUpdate(selectedFontName));
        EventBus.publish(new TitleTextUpdate(text));
    }

    private void updateInfoPreview() {
        String text = infoTextField.getText();
        String selectedFontName = (String) infoFontSelection.getSelectedItem();
        //Font font = new Font((String) infoFontSelection.getSelectedItem(), Font.PLAIN, 72); 
        EventBus.publish(new InfoFontUpdate(selectedFontName));
        EventBus.publish(new InfoTextUpdate(text));
        
    }

    private void createTitleFontSelection(){
        titleFontSelection = createFontSelection();
        EventBus.publish(new TitleFontUpdate((String) titleFontSelection.getSelectedItem()));
    }

    private void createInfoFontSelection(){
        infoFontSelection = createFontSelection();
        EventBus.publish(new InfoFontUpdate((String) infoFontSelection.getSelectedItem()));
    }

    private JComboBox<String> createFontSelection(){
       File folder = new File("resources/misc/fonts");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder not found.");
            return new JComboBox<>(new String[0]);
        }

        Pattern pattern = Pattern.compile("(.+)-(?i)(regular|bold|italic)\\.(ttf|otf)$");
        Set<String> familyNames = new HashSet<String>();

        File[] files = folder.listFiles();
        if (files == null) return new JComboBox<>(new String[0]);

        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                familyNames.add(matcher.group(1));
            }
        }
        List<String> sortedList = new ArrayList<>(familyNames);
        Collections.sort(sortedList);
        return new JComboBox<>(sortedList.toArray(new String[0]));
    }

    private void createCardComponentSelection(){
        cardComponentTabbedPane = new JTabbedPane();

        frameSelect = new CardImageBrowser("resources/img/card_components/frame",360,90,600,40, 64,Card.FRAME_IMAGE);
        backgroundSelect = new CardImageBrowser("resources/img/card_components/background",360,90,600,40, 64,Card.BACKGROUND_IMAGE); 
        //textboxSelect = new CardImageBrowser("resources/img/card_components/textbox",360,90,600,40, 64,"cardTextbox");
        //titleSelect = new CardImageBrowser("resources/img/card_components/title",360,90,600,40, 64,"cardTitle");

        //effectSelect = new CardImageBrowser("resources/img/card_components/effects",360,90,600,40, 64,"cardBackground");

        cardComponentTabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        //cardComponentTabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        //cardComponentTabbedPane.addTab("Choose Title",titleSelect.getScrollPane());
        //cardComponentTabbedPane.addTab("Choose Effect Background",effectSelect.getScrollPane());
    }

    public void onTypeUpdate(SelectTypePanelUpdateEvent e){
        itemArtChangeToType(e.type);
    }

    private void itemArtChangeToType(String type){
        //EventBus.publish(new ImageUpdateEvent(CARD,"resources/"+type+".png"));
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
        //textboxSelect.rescale(scale);

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

    private void onLoadConfig(LoadConfigEvent e){
        ImageComposerConfig config = e.config;

        infoTextField.setText(config.infoText);
        titleTextField.setText(config.titleText);

        titleFontSelection.setSelectedItem(config.titleFont);

        EventBus.publish(new RepaintPanelEvent());
    }
}
