package gui.image_composers.components;

import javax.swing.JButton;

import events.EventBus;
import events.ImageUpdate;

public class ImageUpdateButton extends JButton {
    private String name,id,path;

    public ImageUpdateButton(String name, String id, String path){
        this.name = name;
        setName(name);
        this.id = id;
        this.path= path;

        addActionListener(e -> {
            EventBus.publish(new ImageUpdate(id, path));
        });
    }
}
