package gui.controlpanel1;

import abstractclasses.*;
import gui.GlobalVar;

public class ItemArtImageBrowser extends ImageBrowser{

    public ItemArtImageBrowser(String path, int width, int height, int x, int y, int iconSize) {
        this.path = path;
        this.type = GlobalVar.ITEM_IMAGE;
        this.width = width;
        this.height = height;
        //this.iconSize = iconSize;

        init();
    }
}
