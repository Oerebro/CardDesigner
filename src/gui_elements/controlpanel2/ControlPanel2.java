package gui_elements.controlpanel2;
import javax.swing.*;

import abstractclasses.*;
import gui_elements.CardDesignerGUI;


public class ControlPanel2 extends ControlPanel{

    private JLabel frameLabel, backgroundLabel, textboxLabel;
    private JButton loadFrameButton, loadBackgroundButton, loadTextboxButton, exportButton;
    private CardDesignerGUI parent;

    public void init(CardDesignerGUI parent){
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createButtons();
        compose();
        rescale(1.0);
    }

    private void createButtons(){


        loadFrameButton = new JButton("Load Custom");
        loadBackgroundButton = new JButton("Load Background");
        loadTextboxButton = new JButton("Load Text Box");

        exportButton = new JButton("Export Card");

        frameLabel = new JLabel("No frame selected");
        backgroundLabel = new JLabel("No background selected");
        textboxLabel = new JLabel("No text box selected");
        
        //Listeners
        loadFrameButton.addActionListener(e -> parent.loadImagePreviewPanel("frame"));
        loadBackgroundButton.addActionListener(e -> parent.loadImagePreviewPanel("background"));
        loadTextboxButton.addActionListener(e -> parent.loadImagePreviewPanel("textbox"));

        exportButton.addActionListener(e -> parent.exportImage());

    }
    
    public void compose(){
        add(textboxLabel);
        add(exportButton);

    }

    public void rescale(double scale){
        setBounds((int)(10*scale),(int)(755*scale),(int)(750*scale),(int)(100*scale));
    }
}
