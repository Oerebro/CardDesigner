package gui.controlpanel1;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import gui.*;
import abstractclasses.*;
import events.EventBus;
import events.ImageUpdateEvent;

import java.awt.Color;


public class CardImageBrowser extends ImageBrowser{
    private String type;

    public CardImageBrowser(String path, int width, int height,int x, int y, int iconSize,String type) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.iconSize = iconSize;
        this.type = type;

        init();
        rescale(1.0);
    }


    protected void updateImage(File file) {
        publishImageUpdate(type,path);
        /*switch(type){
            case 'f': publishImageUpdate("cardFrame",file.getPath());break;
            case 'b': publishImageUpdate("cardBackground",file.getPath());break;
            case 't': publishImageUpdate("cardTextbox",file.getPath());break;
            case 'h': publishImageUpdate("cardTitle",file.getPath());break;
        }*/
    }

    @Override
    protected IconLabel addMouseListener(IconLabel label, File file) {
        label.setOpaque(true);  // Important: This allows the background color to show.
        label.setBackground(Color.LIGHT_GRAY);  // Set light grey background.

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                updateImage(file);
            }
        });

        return label;
    }

    private void publishImageUpdate(String type, String path){
        EventBus.publish(new ImageUpdateEvent(type,path));
    }
}
