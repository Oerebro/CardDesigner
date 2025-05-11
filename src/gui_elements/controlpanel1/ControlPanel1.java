package gui_elements.controlpanel1;

//import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.awt.Font; 

//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;

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
    private JTextArea infoTextField;
    private JComboBox<String> titleFontSelection,infoFontSelection;

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
    
        // Item Art VariableTabbedPane with default state weapons
        weapons = new VariableTabbedPane();
        weapons.init(parent, 0);
        selectItemArt = weapons;
    
        // Create title input field and font selector
        createTitleFontSelection();
        createInfoFontSelection();
        createTextFieldsAndPreview();
        
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
        add(infoTextField);

        //infoTextField.setWrapStyleWord(true); // Wrap at word boundaries
        //infoTextField.setLineWrap(true); // Enable wrapping
        infoTextField.setPreferredSize(new java.awt.Dimension(485, 320));
    
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
    
        // Listen for font selection changes
        titleFontSelection.addActionListener(e -> updateTitlePreview());
        infoFontSelection.addActionListener(e -> updateInfoPreview());
    }
    
    //updates the text in the preview Panel
    private void updateTitlePreview() {
        String text = titleTextField.getText();
        String selectedFontName = (String) titleFontSelection.getSelectedItem();
        Font font = new Font(selectedFontName, Font.PLAIN, 72); // Default size, will be resized in PreviewPanel
        parent.updateTitleTextDisplay(text, font);
    }

    /*private void updateInfoPreview() {
        
        String text = infoTextField.getText();
        String selectedFontName = (String) infoFontSelection.getSelectedItem();
        Font font = new Font(selectedFontName, Font.PLAIN, 72); // Default size, will be resized in PreviewPanel
        parent.updateInfoTextDisplay(text, font);
    }*/

    private void updateInfoPreview() {
        String text = infoTextField.getText();
        
        // Format the text as HTML, converting line breaks to <br> tags for proper line breaks in preview
        String formattedText = "<html>" + text.replaceAll("\n", "<br>") + "</html>";
    
        // Assuming parent.updateInfoTextDisplay() is expecting the formatted HTML text and a font
        String selectedFontName = (String) infoFontSelection.getSelectedItem();
        Font font = new Font(selectedFontName, Font.PLAIN, 72);  // Example font size, can be adjusted
    
        // Update the preview with both the formatted text and the selected font
        parent.updateInfoTextDisplay(formattedText, font); // Now passing both formatted text and font
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

        titleFontSelection.setBounds((int) (280 *scale), (int) (290*scale), (int) (90*scale), (int) (30*scale));
        infoFontSelection.setBounds((int) (280 *scale), (int) (335*scale), (int) (90*scale), (int) (30*scale));
        titleTextField.setBounds((int) (10 *scale), (int) (290*scale), (int) (260*scale), (int) (30*scale));
        infoTextField.setBounds((int) (10 *scale), (int) (335*scale), (int) (260*scale), (int) (205*scale));


        //absolute pos of the controlpanel within the window frame
        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1210 * scale), (int)(1070 * scale));
    }
}
