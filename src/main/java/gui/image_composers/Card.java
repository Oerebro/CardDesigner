package gui.image_composers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JTabbedPane;

import events.EventBus;
import events.ImageUpdate;
import events.RepaintPanelEvent;

public class Card extends ComponentLoader{
    private int[] componentBrowserBounds = {0,0,0,0};
    private int[] imageBrowserBounds = {0,0,0,0};
    public final int REPAINT_JCOMPONENT = 1;
    public final int REPAINT_IMAGE = 2;
    protected int baseWidth = 750;
    protected int baseHeight = 1050;
    protected int targetWidth, targetHeight;

    

    public Card(String preset){
        preset = "resources\\card_presets\\"+preset+".json";
        loadComponents(preset);

//initalize empty tabbedpanes where loaded image types can register their source paths
        this.componentImageBrowser = new JTabbedPane();
        componentImageBrowser.setBounds(componentBrowserBounds[0],componentBrowserBounds[1],componentBrowserBounds[2],componentBrowserBounds[3]);
        this.cardImageBrowser = new JTabbedPane();
        cardImageBrowser.setBounds(imageBrowserBounds[0],imageBrowserBounds[1],imageBrowserBounds[2],imageBrowserBounds[3]);


        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
    }

    

    private void onImageUpdate(ImageUpdate e){
        BufferedImage img = getImageFromFile(e.path);
        RenderableImage ri = imageMap.get(e.imageName);
        if (ri != null && img != null) {
            ri.image = img;
        } else {
            System.err.println("Image update failed: " +
                (ri == null ? "No such image name. " : "")+ e.imageName +
                (img == null ? "Image file could not be loaded." : "")
            );
        }
        EventBus.publish(new RepaintPanelEvent());
    }

    public BufferedImage composeCard(double scale, int type){
        return paintAll(scale);
    }

    public BufferedImage composeCard(double scale, int type, String componentID){
        switch(type){
            case REPAINT_JCOMPONENT:
                paintSpecificJComponent(scale, componentID);
                break;
            default:
                return paintSpecificImage(scale,componentID);
        }
        return null;
    }

    private BufferedImage paintAll(double scale) {
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        BufferedImage canvas = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();

        for (RenderableImage ri : imageRenderQueue) {
            if (ri.image != null && ri.bounds != null) {
                int[] b = ri.bounds;
                g2d.drawImage(ri.image, (int) (b[0]*scale), (int) (b[1]*scale), (int) (b[2]*scale), (int) (b[3]*scale), null);
            }
        }

        g2d.dispose();
        return paintWhiteCorners(canvas);
    }

    private BufferedImage paintSpecificImage(double scale, String imageName) {
        targetWidth = (int) (baseWidth * scale);
        targetHeight = (int) (baseHeight * scale);

        BufferedImage canvas = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();

        RenderableImage ri = imageMap.get(imageName);
        int[] b = ri.bounds;
        g2d.drawImage(ri.image, (int) (b[0]*scale), (int) (b[1]*scale), (int) (b[2]*scale), (int) (b[3]*scale), null);

        g2d.dispose();
        return paintWhiteCorners(canvas);
    }

    private BufferedImage paintSpecificJComponent(double scale, String componentName) {
        //Code to render JComponent
        return null;
    }

    private BufferedImage paintWhiteCorners(BufferedImage img) {
        return img;
    }
}
