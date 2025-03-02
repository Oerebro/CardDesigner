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
    private CardImageBrowser frameSelect, backgroundSelect, textboxSelect, titleSelect;
    private VariableTabbedPane selectItemArt;

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
        frameSelect = new CardImageBrowser(parent,"resources/frame",360,180,'f');
        backgroundSelect = new CardImageBrowser(parent,"resources/background",360,180,'b');
        
        textboxSelect = new CardImageBrowser(parent,"resources/textbox",360,180,'t');
        titleSelect = new CardImageBrowser(parent,"resources/title",360,180,'h');
        tabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        tabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        tabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        tabbedPane.addTab("Choose Title",titleSelect.getScrollPane());

        selectItemArt = new VariableTabbedPane();
        selectItemArt.init(parent);

        //selectTypePanel
        selectItemTypePanel = new SelectTypePanel(parent);

        /*  Listeners
        loadFrameButton.addActionListener(e -> parent.loadImagePreviewPanel("frame"));
        loadBackgroundButton.addActionListener(e -> parent.loadImagePreviewPanel("background"));
        loadTextboxButton.addActionListener(e -> parent.loadImagePreviewPanel("textbox"));
        //loadTextboxButton.addActionListener(e -> parent.loadImagePreviewPanel("title"));*/

 
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
