package gui.controlpanel1;

import java.io.File;
import abstractclasses.*;

import java.awt.Color;


public class CardImageBrowser extends ImageBrowser{

    public CardImageBrowser(String path, int width, int height,int x, int y, int iconSize,int type) {
        this.path = path;
        this.width = width;
        this.height = height;
        //this.iconSize = iconSize;
        this.type = type;

        init();
        rescale(1.0);
    }

}
