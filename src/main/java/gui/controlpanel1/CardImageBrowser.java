package gui.controlpanel1;

import java.io.File;
import abstractclasses.*;

import java.awt.Color;


public class CardImageBrowser extends ImageBrowser{

    public CardImageBrowser(String path, int width, int height,int type) {
        this.path = path;

        this.type = type;

        init();
        rescale(1.0);
    }

}
