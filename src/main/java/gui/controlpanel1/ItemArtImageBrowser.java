package gui.controlpanel1;

import java.io.File;
import abstractclasses.*;
import events.EventBus;
import events.ImageUpdateEvent;

public class ItemArtImageBrowser extends ImageBrowser{

    public ItemArtImageBrowser(String path, int width, int height, int x, int y, int iconSize) {
        //super(parent, path, width, height, 'I');
        this.path = path;
        this.width = width;
        this.height = height;
        this.iconSize = iconSize;

        init();
        rescale(1.0);
    }

    protected void updateImage(File file) {
        EventBus.publish(new ImageUpdateEvent("cardItemImage",file.getPath()));
    }
}
