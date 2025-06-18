package gui.controlpanel1;

import abstractclasses.*;
import gui.card_types.Card;

public class ItemArtImageBrowser extends ImageBrowser{

    public ItemArtImageBrowser(String path, int width, int height, int x, int y, int iconSize) {
        //super(parent, path, width, height, 'I');
        this.path = path;
        this.type = Card.ITEM_IMAGE;
        this.width = width;
        this.height = height;
        this.iconSize = iconSize;

        init();
        //rescale(1.0);
    }
}
