package gui.controlpanel1;

import javax.swing.*;

import events.EventBus;
import events.InfoColorUpdate;
import events.TitleColorUpdate;
import gui.*;

import java.awt.*;

public class ColorPicker extends JButton{
    //private CardDesignerGUI parent;

    public ColorPicker(CardDesignerGUI parent, int x, int y,int width,int height, String type){
        this.setBackground(Color.WHITE);  
        this.setFocusPainted(false);
        //this.parent = parent;
        this.setBounds(x,y,width,height);

        this.addActionListener(e -> {
                Color selected = JColorChooser.showDialog(parent.frame, "Choose a color", this.getBackground());
                if (selected != null) {
                    this.setBackground(selected);
                    
                    switch(type){
                        case "title": EventBus.publish(new TitleColorUpdate(selected));break;
                        case "info":  EventBus.publish(new InfoColorUpdate(selected));break;
                    }


                }
            });
    }
}

