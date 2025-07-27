package gui.image_composers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JTabbedPane;

import events.EventBus;
import events.ImageUpdate;
import events.RepaintPanelEvent;
import gui.RenderManager;
import gui.image_composers.components.RenderableImage;

public class Card extends ComponentLoader{
    //private int[] componentBrowserBounds = {0,0,0,0};
    //private int[] imageBrowserBounds = {0,0,0,0};
    public final int REPAINT_JCOMPONENT = 1;
    public final int REPAINT_IMAGE = 2;
    protected int baseWidth = 750;
    protected int baseHeight = 1050;
    protected int targetWidth, targetHeight;

    

    public Card(String preset){
        preset = "resources\\card_presets\\"+preset+".json";
        loadComponents(preset);

        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
    }

    

    private void onImageUpdate(ImageUpdate e){
        BufferedImage img = getImageFromFile(e.path);
        RenderableImage ri = RenderManager.getImageMap().get(e.id);
        if (ri != null && img != null) {
            ri.image = img;
            ri.sourcePath = e.path;
        } else {
            System.err.println("Image update failed: " +
                (ri == null ? "No such image name. " : "")+ e.id +
                (img == null ? " Image file could not be loaded." : " from path "+e.path)
            );
        }
        EventBus.publish(new RepaintPanelEvent("image",ri.render));
    }


}
