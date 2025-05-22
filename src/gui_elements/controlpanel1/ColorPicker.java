package gui_elements.controlpanel1;

import javax.swing.*;

import gui_elements.CardDesignerGUI;

import java.awt.*;
import java.awt.event.*;

public class ColorPicker extends JButton{
    private CardDesignerGUI parent;

    public ColorPicker(CardDesignerGUI parent, int x, int y,int width,int height, String type){
        this.setBackground(Color.WHITE);  
        this.setFocusPainted(false);
        this.parent = parent;
        this.setBounds(x,y,width,height);

        this.addActionListener(e -> {
                Color selected = JColorChooser.showDialog(parent.frame, "Choose a color", this.getBackground());
                if (selected != null) {
                    this.setBackground(selected);
                    
                    switch(type){
                        case "title": parent.setTitleColor(selected);break;
                        case "info": parent.setInfoColor(selected);break;
                    }
                }
            });
    }
}

