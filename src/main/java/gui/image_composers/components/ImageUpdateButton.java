package gui.image_composers.components;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.formdev.flatlaf.util.ScaledImageIcon;

import abstractclasses.TextComponent;
import events.EventBus;
import events.ImageUpdate;

public class ImageUpdateButton extends JButton implements TextComponent{
    private String name,id,path, updateID;

    public ImageUpdateButton(String id, String updateID, String path){
        //setName(name);
        this.id = id;
        this.path= path;
        this.updateID = updateID;
        //this.setText(id);
        setIcon(new ScaledImageIcon(new ImageIcon(path),50,50));

        addActionListener(e -> {
            EventBus.publish(new ImageUpdate(updateID, path));
        });
    }

    public JPanel getInputComponent(){
        JPanel panel = new JPanel();
        panel.add(this);
        return panel;
    }

}
