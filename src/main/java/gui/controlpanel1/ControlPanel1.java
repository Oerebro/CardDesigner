package gui.controlpanel1;

import java.awt.Dimension;
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
import gui.ImageBrowserManager;
import gui.TextComponentManager;
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

    private JTabbedPane cardComponentTabbedPane, selectItemArt;
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
        setLayout(new GridLayout(1,3));
        //left side: all major text components like title, info etc.
        //right side: all minor text components, like damage
        JPanel imageBrowsers = new JPanel();
        imageBrowsers.setLayout(new GridLayout(2,1));
        imageBrowsers.add(ImageBrowserManager.getCardComponents());
        imageBrowsers.add(ImageBrowserManager.getCardImages());
        JPanel leftSide = TextComponentManager.getLeftSide();
        JPanel rightSide = TextComponentManager.getRightSide();
        leftSide.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        
        add(imageBrowsers);
        add(leftSide);
        add(rightSide);

        EventBus.subscribe(CardTypeUpdate.class, this::onTypeUpdate);
        EventBus.subscribe(TextLoadEvent.class, this::onTextLoad);
        EventBus.subscribe(CardLoadEvent.class, this::onCardLoad);
        EventBus.subscribe(TextUpdate.class, this::onTextUpdate);
        

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
    
        // Create title input field and font selector
        createTitleFontSelection();
        createInfoFontSelection();
        
        
    }

    private void onCardLoad(CardLoadEvent e){
        EventBus.publish(new TitleFontUpdate(getTitleFont()));
    }

    public String getTitleFont(){
        return "";
        //return (String) titleFontSelection.getSelectedItem();
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
            attributePanel.revalidate();
            attributeSelectionPanel.add(attributePanel);
            attributeSelectionPanel.repaint();
            this.repaint();
            SwingUtilities.invokeLater(() -> {EventBus.publish(new CardTypeUpdate(type));});
            
        }    
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
        cardComponentTabbedPane = ImageBrowserManager.getCardComponents();

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
        //remove(selectItemArt);

        switch(type){
            /*case GlobalVar.WEAPON: 
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
            //case "character":*/
        }

        add(selectItemArt);
        double scale = parent.getFrameScale();
        selectItemArt.setBounds((int) (390 *scale), (int) (0), (int) (360*scale), (int) (245*scale));
        selectItemArt.revalidate();
        selectItemArt.repaint();
        this.repaint();
    }
    
    public void rescale(double scale) {

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
