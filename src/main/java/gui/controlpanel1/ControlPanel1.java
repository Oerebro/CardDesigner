package gui.controlpanel1;

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
import abstractclasses.*;
import events.CardLoadEvent;
import events.EventBus;
import events.FontSizeUpdate;
import events.InfoFontUpdate;
import events.SelectTypePanelUpdateEvent;
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
    private VariableTabbedPane selectItemArt, weapons,rune, armor, accessoire, consumable,effect,character;
    private JTextField titleTextField,typeTextField;
    private JTextArea infoTextField;
    private JComboBox<String> titleFontSelection,infoFontSelection;
    private JCheckBox titleStroke, infoStroke, typeStroke;
    private ColorPicker titleColor, infoColor;

    private int[] titleFieldBounds = {10,290,305,30};
    private int[] typeFieldBounds = {10,335,305,30};
    private int[] infoFieldBounds = {10,380,305,205};

    private int[] titleFontBounds = {320,290,90,30};
    private int[] infoFontBounds = {320, 380, 90, 30};

    private int[] titleColorBounds = {420, 290, 30, 30};
    private int[] infoColorBounds = { 420, 380, 30, 30};
    

    public void init(CardDesignerGUI parent) {
        EventBus.subscribe(SelectTypePanelUpdateEvent.class, this::onTypeUpdate);
        EventBus.subscribe(TextLoadEvent.class, this::onTextLoad);
        EventBus.subscribe(CardLoadEvent.class, this::onCardLoad);
        
        
        
        setLayout(null);
        this.parent = parent;
        createButtons();
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
        add(selectItemTypePanel);
        add(selectItemArt);
        add(titleFontSelection);
        add(infoFontSelection);
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

        titleColor = new ColorPicker(parent, 420, 290, 30, 30, "title");
        infoColor = new ColorPicker(parent, 420, 335, 30, 30, "info");

        titleStroke = new JCheckBox("Title Outline",false);
        titleStroke.setBounds(460,290,100,30);
        titleStroke.addActionListener(e->{EventBus.publish(new ToggleTextBorder(GlobalVar.TITLE_BORDER,titleStroke.isSelected()));});

        typeStroke = new JCheckBox("Type Info Outline",false);
        typeStroke.setBounds(460,335,100,30);
        typeStroke.addActionListener(e->{EventBus.publish(new ToggleTextBorder(GlobalVar.TYPE_BORDER,typeStroke.isSelected()));});

        infoStroke = new JCheckBox("Rules Text Outline",false);
        infoStroke.setBounds(460,380,100,30);
        infoStroke.addActionListener(e->{EventBus.publish(new ToggleTextBorder(GlobalVar.INFO_BORDER,infoStroke.isSelected()));});

        JButton fontUp = new JButton("+");
        JButton fontDown = new JButton("-");
        fontUp.setBounds(infoFontBounds[0],425,30,30);
        fontUp.addActionListener(e->{EventBus.publish(new FontSizeUpdate('+'));});
        fontDown.setBounds(infoFontBounds[0]+40,425,30,30);
        fontDown.addActionListener(e->{EventBus.publish(new FontSizeUpdate('-'));});
        add(fontUp);
        add(fontDown);
        add(titleStroke);
        add(typeStroke);
        add(infoStroke);
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

        frameSelect = new CardImageBrowser(GlobalVar.CARD_COMPONENTS_IMAGE_PATH+"frame",360,90,600,40, 64,GlobalVar.FRAME_IMAGE);
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
        cardComponentTabbedPane.addTab("Choose Effect Background",effectSelect.getScrollPane());
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
        selectItemTypePanel.setBounds((int) (770 *scale), (int) (0), (int) (300*scale), (int) (1000*scale));

        setComponentBounds(titleFontSelection, titleFontBounds, scale);
        setComponentBounds(infoFontSelection, infoFontBounds, scale);

        setComponentBounds(titleTextField, titleFieldBounds, scale);
        setComponentBounds(typeTextField, typeFieldBounds, scale);
        setComponentBounds(infoTextField, infoFieldBounds, scale);

        setComponentBounds(infoColor, infoColorBounds, scale);
        setComponentBounds(titleColor, titleColorBounds, scale);


        //absolute pos of the controlpanel within the window frame
        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1210 * scale), (int)(1070 * scale));
    }

    private void setComponentBounds(JComponent c, int[] bounds, double scale){
        c.setBounds((int)(bounds[0]*scale),(int)(bounds[1]*scale),(int)(bounds[2]*scale),(int)(bounds[3]*scale));
    }

}
