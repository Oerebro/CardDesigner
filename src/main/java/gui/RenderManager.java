package gui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import abstractclasses.TextComponent;
import events.EventBus;
import events.RepaintPanelEvent;
import gui.image_composers.components.RenderableImage;
import gui.image_composers.components.RenderableText;
import java.awt.Rectangle;

public class RenderManager {
//this keeps track which image is assigned to which id (i.e. cardBackground etc)
    private static Map<String, RenderableImage> imageMap = new HashMap<>();

//keeps track of which text is assigned to which id (cardTitle etc)
    private static Map<String, RenderableText> textMap = new HashMap<>();

    public static void addToImageMap(RenderableImage img){
        if(!imageMap.containsKey(img.id)){
            imageMap.put(img.id, img);
            EventBus.publish(new RepaintPanelEvent("image",img.render));
        }
    }

    public static void addToTextMap(RenderableText txt){
        if(!textMap.containsKey(txt.id)){
            textMap.put(txt.id, txt);
        }
    }

    public static Map<String,RenderableImage> getImageMap(){
        return imageMap;
    }

    public static Map<String,RenderableText> getTextMap(){
        return textMap;
    }

    public static BufferedImage renderAll(int baseWidth, int baseHeight, double scale){
        baseWidth = (int) (baseWidth*scale);
        baseHeight = (int) (baseHeight*scale);

        BufferedImage img = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        for(int i=0; i<=10;i++){
            g2d.drawImage(renderImageLayer(baseWidth, baseHeight, i, scale), 0, 0, baseWidth,baseHeight,null);
            g2d.drawImage(renderTextLayer(baseWidth, baseHeight, i, scale), 0, 0, baseWidth,baseHeight,null);
        }

        return img;
    }

    public static BufferedImage renderImageLayer(int baseWidth, int baseHeight, int renderLayer, double scale){
        BufferedImage image = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        int[] bounds;

        for (Map.Entry<String, RenderableImage> entry : RenderManager.getImageMap().entrySet()) {
            RenderableImage img = entry.getValue();
            if (img.render == renderLayer) {
                bounds = img.bounds;
                g2d.drawImage(img.image,(int) (bounds[0]*scale),(int) (bounds[1]*scale),(int) (bounds[2]*scale),(int) (bounds[3]*scale),null);
            }
        }
        g2d.dispose();
        return paintWhiteCorners(image);
    }

    public static BufferedImage renderTextLayer(int baseWidth, int baseHeight, int renderLayer, double scale){
        BufferedImage image = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage i;
        Graphics2D g2d = image.createGraphics();
        int[] bounds;

        for (Map.Entry<String, RenderableText> entry : RenderManager.getTextMap().entrySet()) {
            RenderableText txt = entry.getValue();
            if (txt.render == renderLayer) {
                JComponent comp = txt.component;
                if (comp instanceof TextComponent) {
                    String text = ((TextComponent) comp).getText();
                    //((TextComponent) comp).scaleForPaint(scale);
                }
                bounds = txt.bounds;
                i = new BufferedImage((int) (bounds[2] * scale), (int) (bounds[3] * scale), BufferedImage.TYPE_INT_ARGB);
                Graphics2D labelGraphics = i.createGraphics();

                /*try {
                    Method method = comp.getClass().getMethod("scaleFont", double.class);
                    method.invoke(comp,scale);
                } catch (NoSuchMethodException e) {
                    comp.setSize( (int) (bounds[2] * scale),(int) (bounds[3] * scale));
                } catch (Exception e) {
                    e.printStackTrace(); 
                }*/
                comp.setSize( (int) (bounds[2] * scale),(int) (bounds[3] * scale));
                comp.doLayout();
                comp.revalidate();
    
                comp.printAll(labelGraphics);
                labelGraphics.dispose();
                    //add check for stroke outline here
                g2d.drawImage(i,(int) (bounds[0]*scale),(int) (bounds[1]*scale),(int) (bounds[2]*scale),(int) (bounds[3]*scale),null);
            }
        }
        g2d.dispose();
        return image;
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
