package gui_elements.controlpanel1;

import java.awt.Dimension;

import javax.swing.*;

import abstractclasses.*;
import gui_elements.CardDesignerGUI;

public class ControlPanel1 extends ControlPanel {

    private JLabel frameLabel, backgroundLabel, textboxLabel;
    private JButton loadFrameButton, loadBackgroundButton, loadTextboxButton;
    private CardDesignerGUI parent;
    
    private SelectTypePanel typeCheckboxPanel;

    private JTabbedPane tabbedPane, tabbedPaneItemArt;
    private ImageBrowser frameSelect, backgroundSelect, textboxSelect, titleSelect, itemArtSelect;

    public void init(CardDesignerGUI parent) {
        this.parent = parent;
        createButtons();
        compose();
        rescale(1.0);
    }

    private void createButtons() {
        // Create the dropdown menu for frame selection
        tabbedPane = new JTabbedPane();
        frameSelect = new ImageBrowser(parent,"resources/frame",360,180,'f');
        backgroundSelect = new ImageBrowser(parent,"resources/background",360,180,'b');
        
        textboxSelect = new ImageBrowser(parent,"resources/textbox",360,180,'t');
        titleSelect = new ImageBrowser(parent,"resources/title",360,180,'h');
        tabbedPane.addTab("Choose Frame",frameSelect.getScrollPane());
        tabbedPane.addTab("Choose Background",backgroundSelect.getScrollPane());
        tabbedPane.addTab("Choose Textbox",textboxSelect.getScrollPane());
        tabbedPane.addTab("Choose Title",titleSelect.getScrollPane());

        //create initial tabs and implement lazy loading

        tabbedPaneItemArt = new JTabbedPane();
        //bow




        // Create Checkboxes for the card type
        typeCheckboxPanel = new SelectTypePanel(parent);

        loadFrameButton = new JButton("Load Custom");
        loadBackgroundButton = new JButton("Load Background");
        loadTextboxButton = new JButton("Load Text Box");

        frameLabel = new JLabel("No frame selected");
        backgroundLabel = new JLabel("No background selected");
        textboxLabel = new JLabel("No text box selected");

        // Listeners
        loadFrameButton.addActionListener(e -> parent.loadImagePreviewPanel("frame"));
        loadBackgroundButton.addActionListener(e -> parent.loadImagePreviewPanel("background"));
        loadTextboxButton.addActionListener(e -> parent.loadImagePreviewPanel("textbox"));
        //loadTextboxButton.addActionListener(e -> parent.loadImagePreviewPanel("title"));

 
    }

    public void compose() {
        System.out.println("Compose");
        add(tabbedPane);
        //add(tabbedPaneItemArt);
        // add(titlebox.inputField);
        add(loadFrameButton);
        add(typeCheckboxPanel.menu);
        add(frameLabel);
        add(loadBackgroundButton);
        add(backgroundLabel);
        add(loadTextboxButton);
        add(textboxLabel);
    }

    public void rescale(double scale) {
        System.out.println("rescale");
        frameSelect.rescale(scale);
        backgroundSelect.rescale(scale);
        textboxSelect.rescale(scale);
        tabbedPane.setPreferredSize(new Dimension((int)(360 * scale), (int)(180 * scale)));
        tabbedPane.setBounds((int)(575 * scale), (int)(10 * scale), (int)(360 * scale), (int)(180 * scale));
        
        tabbedPaneItemArt.setBounds((int) (980*scale), (int) (260*scale), (int) (360*scale), (int) (500*scale));
        setBounds((int)(575 * scale), (int)(10 * scale), (int)(1100 * scale), (int)(1070 * scale));
    }
}
