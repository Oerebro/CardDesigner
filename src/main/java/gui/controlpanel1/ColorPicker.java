package gui.controlpanel1;

import javax.swing.*;

import events.EventBus;
import events.InfoColorUpdate;
import events.ColorUpdate;
import gui.*;

import java.awt.*;

public class ColorPicker extends JButton{
    //private CardDesignerGUI parent;

    public ColorPicker(int width,int height, String id){
        this.setBackground(Color.BLACK);  
        this.setFocusPainted(false);
        Dimension dim = new Dimension(width,height);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim); 

        this.addActionListener(e -> {
                Color selected = JColorChooser.showDialog(this, "Choose a color", this.getBackground());
                if (selected != null) {
                    this.setBackground(selected);
                    
                    EventBus.publish(new ColorUpdate(id,selected));


                }
            });
    }

}

