package gui.controlpanel2;

import javax.swing.*;

import gui.CardDesignerGUI;
import abstractclasses.*;


public class ControlPanel2 extends ControlPanel{

    private JLabel textboxLabel;
    private JButton  exportButton;
    private CardDesignerGUI parent;

    public void init(CardDesignerGUI parent){
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createButtons();
        compose();
        rescale(1.0);
    }

    private void createButtons(){


        exportButton = new JButton("Export Card");
        textboxLabel = new JLabel("No text box selected");
        
        //Listeners
       /*  loadFrameButton.addActionListener(e -> parent.loadImagePreviewPanel("frame"));
        loadBackgroundButton.addActionListener(e -> parent.loadImagePreviewPanel("background"));
        loadTextboxButton.addActionListener(e -> parent.loadImagePreviewPanel("textbox"));*/

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
