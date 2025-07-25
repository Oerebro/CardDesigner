package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import events.EventBus;
import events.RepaintPanelEvent;
import gui.image_composers.components.RenderableImage;
import gui.image_composers.components.RenderableText;

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

    public static BufferedImage renderLayer(int baseWidth, int baseHeight, int renderLayer, double scale){
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
        System.out.println(image);
        return image;
    }
}
