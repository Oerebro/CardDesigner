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

        EventBus.subscribe(ImageUpdate.class, this::onImageUpdate);
    }

    

    private void onImageUpdate(ImageUpdate e){
        System.out.println("setting "+e.id+" to "+e.path);
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

    public static BufferedImage paintWhiteCorners(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();

        double realWidthMM = 66.0;
        double realHeightMM = 88.0;

        // Calculate pixels per mm (average)
        double pxPerMM_X = width / realWidthMM;
        double pxPerMM_Y = height / realHeightMM;
        double pxPerMM = (pxPerMM_X + pxPerMM_Y) / 2.0;

        int r = (int) Math.round(3.0 * pxPerMM);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        try {
            g2.drawImage(input, 0, 0, null);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set transparent composite
            g2.setComposite(AlphaComposite.Clear);

            // Top-left corner
            Area tl = new Area(new Rectangle(0, 0, r, r));
            Shape tlCircle = new Ellipse2D.Double(0, 0, 2 * r, 2 * r);
            tl.subtract(new Area(tlCircle));
            g2.fill(tl);

            // Top-right corner
            Area tr = new Area(new Rectangle(width - r, 0, r, r));
            Shape trCircle = new Ellipse2D.Double(width - 2 * r, 0, 2 * r, 2 * r);
            tr.subtract(new Area(trCircle));
            g2.fill(tr);

            // Bottom-right corner
            Area br = new Area(new Rectangle(width - r, height - r, r, r));
            Shape brCircle = new Ellipse2D.Double(width - 2 * r, height - 2 * r, 2 * r, 2 * r);
            br.subtract(new Area(brCircle));
            g2.fill(br);

            // Bottom-left corner
            Area bl = new Area(new Rectangle(0, height - r, r, r));
            Shape blCircle = new Ellipse2D.Double(0, height - 2 * r, 2 * r, 2 * r);
            bl.subtract(new Area(blCircle));
            g2.fill(bl);

        } finally {
            g2.dispose();
        }

        return output;
    }
}
