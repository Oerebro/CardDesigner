package gui.controlpanel1;

import java.awt.FlowLayout;
import java.awt.GridLayout;
//import java.io.File;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;

import gui.CardDesignerGUI;
import gui.GlobalVar;
import gui.previewpanel.DigitOnlyTextField;
import abstractclasses.*;
import events.CardLoadEvent;
import events.EventBus;
import events.FontSizeUpdate;
import events.InfoFontUpdate;
import events.CardTypeUpdate;
import events.TextLoadEvent;
import events.TextUpdate;
import events.TitleFontUpdate;
import events.ToggleTextBorder;

public class ControlPanel1 extends ControlPanel {

    //private JLabel frameLabel, backgroundLabel, textboxLabel;
    //private JButton loadFrameButton, loadBackgroundButton, loadTextboxButton;
    private CardDesignerGUI parent;
    
    
    private SelectTypePanel selectItemTypePanel;

    private JTabbedPane cardComponentTabbedPane;
    private ImageBrowser frameSelect, backgroundSelect, textboxSelect, titleSelect,effectSelect, crownSelect;
    private VariableTabbedPane selectItemArt, weapons,rune, arkham, armor, accessoire, consumable,effect,character;
    private JTextField titleTextField,typeTextField;
    private DigitOnlyTextField fontSizeManual;
    private JTextArea infoTextField;
    private JComboBox<String> titleFontSelection,infoFontSelection;
    private JCheckBox titleStroke, infoStroke, typeStroke;
    private ColorPicker titleColor, infoColor;

    private CardAttributesPanel weaponAtt;
    private CardAttributesPanel armorAtt;
    private CardAttributesPanel accessoireAtt;
    private CardAttributesPanel consumableAtt;
    private CardAttributesPanel runeAtt;
    private CardAttributesPanel effectAtt;
    private CardAttributesPanel attributePanel, arkhamAtt;

    private JPanel attributeSelectionPanel;
    private int cardType = 0;

    private int[] titleTextBounds = {10, 340, 305, 30};
    private int[] typeTextBounds = {10, 385, 305, 30};
    private int[] infoTextBounds = {10, 430, 305, 205};

    private int[] titleFontBounds = {320, 340, 90, 30};
    private int[] infoFontBounds = {320, 430, 90, 30};

    private int[] titleColorBounds = {420, 340, 30, 30};
    private int[] infoColorBounds = {420, 430, 30, 30};
    private int[] typeColorBounds = {420, 385, 30, 30};

    

    public void init(int type, CardDesignerGUI parent) {
        this.parent = parent;
        //this.cardType = type;
        
        EventBus.subscribe(CardTypeUpdate.class, this::onTypeUpdate);
        EventBus.subscribe(TextLoadEvent.class, this::onTextLoad);
        EventBus.subscribe(CardLoadEvent.class, this::onCardLoad);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        
        weaponAtt = new CardAttributesPanel(GlobalVar.WEAPON);
        armorAtt = new CardAttributesPanel(GlobalVar.ARMOR);
        accessoireAtt = new CardAttributesPanel(GlobalVar.ACCESSOIRE);
        consumableAtt = new CardAttributesPanel(GlobalVar.WEAPON);
        runeAtt = new CardAttributesPanel(GlobalVar.RUNE);
        effectAtt = new CardAttributesPanel(GlobalVar.EFFECT);
        arkhamAtt = new CardAttributesPanel(GlobalVar.ARKHAM);
        selectItemArt = new VariableTabbedPane();
        
        setLayout(null);
        createButtons();
        updatePanel(type);
        compose();
        rescale(1.0);
    }

    private void onTextLoad(TextLoadEvent e){
        switch(e.type){
            case TextLoadEvent.INFO: infoTextField.setText(e.text); break;
            case TextLoadEvent.TITLE: titleTextField.setText(e.text); break;
            case TextLoadEvent.TYPE: typeTextField.setText(e.text); break;
        }
    }

    private void onTextUpdate(TextUpdate e){
        if(!(e.type == GlobalVar.FONTSIZE_FIELD_UPDATE)) return;
        fontSizeManual.setText(e.text);
    }

    private void createButtons() {
        createCardComponentSelection();
        attributeSelectionPanel = new JPanel();
        selectItemTypePanel = new SelectTypePanel(this);
        attributeSelectionPanel.setLayout(new FlowLayout());
        attributeSelectionPanel.add(selectItemTypePanel);
        attributePanel = new CardAttributesPanel(cardType);
        attributeSelectionPanel.add(attributePanel);
        this.add(attributeSelectionPanel);
        attributeSelectionPanel.setBounds((int) (770), (int) (0), (int) (400), (int) (2000));
    
        // Item Art VariableTabbedPane with default state weapons
        //weapons = new VariableTabbedPane();
        //weapons.init(GlobalVar.WEAPON);
        
    
        // Create title input field and font selector
        createTitleFontSelection();
        createInfoFontSelection();
        createTextFieldsAndPreview();
        
        
    }

    private void onCardLoad(CardLoadEvent e){
        EventBus.publish(new TitleFontUpdate(getTitleFont()));
    }

    public String getTitleFont(){
        return (String) titleFontSelection.getSelectedItem();
    }

    public Boolean getTitleStroke(){
        return titleStroke.isSelected();
    }

    public void compose() {
        add(cardComponentTabbedPane);
        //add(selectItemTypePanel);
        add(selectItemArt);
        add(titleFontSelection);
        add(infoFontSelection);
    }

    public void updatePanel(int type){
        if(cardType != type){
            this.cardType = type;
            attributeSelectionPanel.remove(attributePanel);
            attributePanel = decideAttPanel(type);
            attributePanel.revalidate();
            attributeSelectionPanel.add(attributePanel);
            attributeSelectionPanel.repaint();
            this.repaint();
            SwingUtilities.invokeLater(() -> {EventBus.publish(new CardTypeUpdate(type));});
            
        }    
    }

    private CardAttributesPanel decideAttPanel(int type){
        switch(type){
            case GlobalVar.WEAPON:
                return weaponAtt;
            case GlobalVar.ARMOR:
                return armorAtt;
            case GlobalVar.ACCESSOIRE:
                return accessoireAtt;
            case GlobalVar.RUNE :
                return runeAtt;
            case GlobalVar.EFFECT:
                return effectAtt;
            case GlobalVar.CONSUMABLE:
                return consumableAtt;
            case GlobalVar.ARKHAM:
                return arkhamAtt;
        }

        return null;
    }

    private void createTextFieldsAndPreview() {
        titleTextField = new JTextField();
        //titleTextField.setBounds(10, 290, 260, 30);
        add(titleTextField);

        typeTextField = new JTextField();
        //titleTextField.setBounds(10, 290, 260, 30);
        add(typeTextField);

        infoTextField = new JTextArea();
        infoTextField.setPreferredSize(new java.awt.Dimension(485, 320));
        infoTextField.setLineWrap(true);
        infoTextField.setWrapStyleWord(true);
        infoTextField.setBorder(UIManager.getBorder("TextField.border"));
        add(infoTextField);

        titleColor = new ColorPicker(parent, titleColorBounds[0], titleColorBounds[1],titleColorBounds[2],titleColorBounds[3],GlobalVar.TITLE_TEXT_UPDATE);
        infoColor = new ColorPicker(parent, infoColorBounds[0], infoColorBounds[1],infoColorBounds[2],infoColorBounds[3], GlobalVar.INFO_TEXT_UPDATE);
        ColorPicker typeColor = new ColorPicker(parent, typeColorBounds[0], typeColorBounds[1],typeColorBounds[2],typeColorBounds[3], GlobalVar.TYPE_TEXT_UPDATE);

        titleStroke = new JCheckBox("Title Outline",true);
        titleStroke.setBounds(460,290,100,30);
        titleStroke.addActionListener(e->{EventBus.publish(new ToggleTextBorder(GlobalVar.TITLE_BORDER,titleStroke.isSelected()));});

        typeStroke = new JCheckBox("Type Info Outline",true);
        typeStroke.setBounds(460,335,100,30);
        typeStroke.addActionListener(e->{EventBus.publish(new ToggleTextBorder(GlobalVar.TYPE_BORDER,typeStroke.isSelected()));});

        infoStroke = new JCheckBox("Rules Text Outline",true);
        infoStroke.setBounds(460,380,100,30);
        infoStroke.addActionListener(e->{EventBus.publish(new ToggleTextBorder(GlobalVar.INFO_BORDER,infoStroke.isSelected()));});

        JButton fontUp = new JButton("+");
        JButton fontDown = new JButton("-");
        fontUp.setBounds(infoFontBounds[0],425+50,30,30);
        fontUp.addActionListener(e->{EventBus.publish(new TextUpdate(GlobalVar.FONTSIZE_TEXT_UPDATE,"+"));});
        fontDown.setBounds(infoFontBounds[0]+40,425+50,30,30);
        fontDown.addActionListener(e->{EventBus.publish(new TextUpdate(GlobalVar.FONTSIZE_TEXT_UPDATE,"-"));});
        fontSizeManual = new DigitOnlyTextField();
        fontSizeManual.setBounds(infoFontBounds[0],460+50,60,30);
        fontSizeManual.setText("19");
        add(fontSizeManual);
        add(fontUp);
        add(fontDown);
        add(titleStroke);
        add(typeStroke);
        add(infoStroke);
        add(titleColor);
        add(infoColor);
        add(typeColor);

    
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

        typeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTypePreview();
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTypePreview();
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTypePreview();
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

        fontSizeManual.addActionListener(e -> {
            if (fontSizeManual.isFocusOwner()) {
                EventBus.publish(new TextUpdate(GlobalVar.FONTSIZE_TEXT_UPDATE, fontSizeManual.getText()));
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
        EventBus.publish(new TextUpdate(GlobalVar.TITLE_TEXT_UPDATE,text));
    }

    private void updateTypePreview() {    
        String text = typeTextField.getText();
        //String selectedFontName = (String) titleFontSelection.getSelectedItem();
        //Font font = new Font(selectedFontName, Font.PLAIN, 72);
        
        EventBus.publish(new TextUpdate(GlobalVar.TYPE_TEXT_UPDATE,text));
    }

    private void updateInfoPreview() {
        String text = infoTextField.getText();
        String selectedFontName = (String) infoFontSelection.getSelectedItem();
        //Font font = new Font((String) infoFontSelection.getSelectedItem(), Font.PLAIN, 72); 
        EventBus.publish(new InfoFontUpdate(selectedFontName));
        EventBus.publish(new TextUpdate(GlobalVar.INFO_TEXT_UPDATE,text));
        
    }

    private void createTitleFontSelection(){
        titleFontSelection = createFontSelection();
        EventBus.publish(new TitleFontUpdate((String) titleFontSelection.getSelectedItem()));
    }

    private void createInfoFontSelection(){
        infoFontSelection = createFontSelection();
        infoFontSelection.setSelectedItem("PlantinMTPro");
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

        /*frameSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"frame",360,90,600,40, 64,GlobalVar.FRAME_IMAGE);
        backgroundSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"background",360,90,600,40, 64,GlobalVar.BACKGROUND_IMAGE); 
        textboxSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"textbox",360,90,600,40, 64,GlobalVar.TEXTBOX_IMAGE);
        titleSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"title",360,90,600,40, 64,GlobalVar.TITLE_IMAGE);
        crownSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"crowns",360,90,600,40, 64,GlobalVar.CROWN_IMAGE);

        effectSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"effects",360,90,600,40, 64,GlobalVar.BACKGROUND_IMAGE);

        cardComponentTabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Title",titleSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Crown",crownSelect.getScrollPane());
        cardComponentTabbedPane.addTab("Choose Effect Background",effectSelect.getScrollPane());*/
    }

    public void onTypeUpdate(CardTypeUpdate e){
        itemArtChangeToType(e.type);
    }

    private void itemArtChangeToType(int type){
        //EventBus.publish(new ImageUpdateEvent(CARD,"resources/"+type+".png"));
        remove(selectItemArt);

        switch(type){
            case GlobalVar.WEAPON: 
                if(weapons == null) {
                    weapons = new VariableTabbedPane(); 
                    weapons.init(type);
                } 
                selectItemArt = weapons; 
                break;
            case GlobalVar.ARMOR: 
                if(armor == null) {
                    armor = new VariableTabbedPane(); 
                    armor.init(type);
                } selectItemArt = armor;   
                break;
            case GlobalVar.ACCESSOIRE: 
                if(accessoire == null) {
                    accessoire = new VariableTabbedPane(); accessoire.init(type);
                } 
                selectItemArt = accessoire;   
                break;
            case GlobalVar.CONSUMABLE: 
                if(consumable == null) {
                    consumable = new VariableTabbedPane(); 
                    consumable.init(type);
                } 
                selectItemArt = consumable;   
                break;
            case GlobalVar.RUNE: 
                if(rune == null) {
                    rune = new VariableTabbedPane(); rune.init(type);
                } 
                selectItemArt = rune;   
                break;
            case GlobalVar.ARKHAM: 
                if(arkham == null) {
                    arkham = new VariableTabbedPane(); arkham.init(type);
                } 
                selectItemArt = arkham;   
                break;
            //case "effect":
            //case "character":
        }

        add(selectItemArt);
        double scale = parent.getFrameScale();
        selectItemArt.setBounds((int) (390 *scale), (int) (0), (int) (360*scale), (int) (245*scale));
        selectItemArt.revalidate();
        selectItemArt.repaint();
        this.repaint();
    }
    
    public void rescale(double scale) {
        //these rescales should be done in a better way, but they work atm
        frameSelect.rescale(scale);
        backgroundSelect.rescale(scale);
        //textboxSelect.rescale(scale);

        //set the absolute position of these menus within the controlpanel
        cardComponentTabbedPane.setBounds((int)(10 * scale), (int)(0), (int)(360 * scale), (int)(295 * scale));   
        selectItemArt.setBounds((int) (390 *scale), (int) (0), (int) (360*scale), (int) (295*scale));
        //selectItemTypePanel.setBounds((int) (770 *scale), (int) (0), (int) (400*scale), (int) (600*scale));
        attributeSelectionPanel.setBounds((int) (770 *scale), (int) (0), (int) (400*scale), (int) (2000*scale));

        setComponentBounds(titleFontSelection, titleFontBounds, scale);
        setComponentBounds(infoFontSelection, infoFontBounds, scale);

        setComponentBounds(titleTextField, titleTextBounds, scale);
        setComponentBounds(typeTextField, typeTextBounds, scale);
        setComponentBounds(infoTextField, infoTextBounds, scale);

        setComponentBounds(infoColor, infoColorBounds, scale);
        setComponentBounds(titleColor, titleColorBounds, scale);


        //absolute pos of the controlpanel within the window frame
        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1210 * scale), (int)(1070 * scale));
    }

    private void setComponentBounds(JComponent c, int[] bounds, double scale){
        c.setBounds((int)(bounds[0]*scale),(int)(bounds[1]*scale),(int)(bounds[2]*scale),(int)(bounds[3]*scale));
    }

}
