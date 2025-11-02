package gui.controlpanel1;

import javax.swing.*;

import events.EventBus;
import events.InfoColorUpdate;
import events.ColorUpdate;
import gui.*;

import java.awt.*;

public class ColorPicker extends JButton{
    private String id;
    //private CardDesignerGUI parent;

    public ColorPicker(int width,int height, String id){
        this.setBackground(Color.BLACK);  
        init(width,height,id);
    }

    public ColorPicker(Color color, int width,int height, String id){
        this.setBackground(color);  
        init(width,height,id);
    }

    public void setColor(Color color){
        this.setBackground(color);
        EventBus.publish(new ColorUpdate(this.id, this.getBackground()));
    }

    private void init(int width, int height, String id){
        this.setFocusPainted(false);
        Dimension dim = new Dimension(width,height);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim); 
        this.id = id;

        this.addActionListener(e -> {
                Color selected = JColorChooser.showDialog(this, "Choose a color", this.getBackground());
                if (selected != null) {
                    this.setBackground(selected);
                    
                    EventBus.publish(new ColorUpdate(id,selected));


                }
            });
        EventBus.publish(new ColorUpdate(id, this.getBackground()));
    }

}

